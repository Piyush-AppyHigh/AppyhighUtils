
package com.appyhigh.mylibrary.audio

import android.annotation.TargetApi
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.appyhigh.mylibrary.misc.VersionUtils


@RequiresApi(Build.VERSION_CODES.M)
fun AudioManager.getOutputAudioDevice(): Array<out AudioDeviceInfo>? {
    return this.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
}

@TargetApi(Build.VERSION_CODES.M)
fun AudioManager.getSampleRate(): Int {
    val defaultSampleRate = 44100
    return if (VersionUtils.isMarshmallowOrUp()) {
        val sampleRateStr: String? = this.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE)
        sampleRateStr?.let { str ->
            Integer.parseInt(str).takeUnless { it == 0 }
        } ?: defaultSampleRate // Use a default value if property not found
    } else {
        defaultSampleRate
    }

}

@TargetApi(Build.VERSION_CODES.M)
fun AudioManager.getFramesPerBuffer(): Int {
    val defaultBufferSize = 256
    return if (VersionUtils.isMarshmallowOrUp()) {
        val framesPerBuffersStr: String? =
                this.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER)
        framesPerBuffersStr?.let { str ->
            Integer.parseInt(str).takeUnless { it == 0 }
        } ?: defaultBufferSize // Use default
    } else {
        defaultBufferSize
    }
}

@TargetApi(Build.VERSION_CODES.M)
fun AudioManager.getOutputLatencyOnAudioRouteChange(audioDevices: Array<out AudioDeviceInfo>?): Int {
    var systemOutputLatency = 0

    if (VersionUtils.isMarshmallowOrUp()) {
        audioDevices?.forEach {
            when (it.type) {
            // Bluetooth
                AudioDeviceInfo.TYPE_BLUETOOTH_A2DP -> systemOutputLatency =
                        getSystemOutputLatencyInMs()
                AudioDeviceInfo.TYPE_BLUETOOTH_SCO -> systemOutputLatency = getSystemOutputLatencyInMs()

            // HDMI
                AudioDeviceInfo.TYPE_HDMI -> systemOutputLatency = getSystemOutputLatencyInMs()
                AudioDeviceInfo.TYPE_HDMI_ARC -> systemOutputLatency = getSystemOutputLatencyInMs()

            //Wired Headphones
                AudioDeviceInfo.TYPE_WIRED_HEADPHONES -> systemOutputLatency =
                        getSystemOutputLatencyInMs()

            // Wired Headset
                AudioDeviceInfo.TYPE_WIRED_HEADSET -> systemOutputLatency = getSystemOutputLatencyInMs()
            }
        }
    }
    return systemOutputLatency
}

/**
 * Retrieve system latency from the Audio Manager using reflection to access the hidden system api
 * Refer to: https://stackoverflow.com/questions/15802659/how-do-you-determine-the-audio-latency-audiotrack-on-android/37625791
 *
 * This api is currently added to greylist in Android P
 * Link: https://android.googlesource.com/platform/frameworks/base/+/master/config/hiddenapi-p-light-greylist.txt#3140
 *
 * @return Int latency in milliseconds
 */
fun AudioManager.getSystemOutputLatencyInMs(): Int {
    return try {
        val method = this.javaClass.getMethod("getOutputLatency", Int::class.javaPrimitiveType)
        method.invoke(this, AudioManager.STREAM_MUSIC) as Int
    } catch (e: Exception) {
        Log.d("AudioManager", "System Latency not reported by the system, default to 0 ms")
        0
    }
}

/**
 * Check if the music audio stream is muted
 * @return Boolean
 */
@RequiresApi(Build.VERSION_CODES.M)
fun AudioManager.isStreamMuted(): Boolean {
    return if (VersionUtils.isMarshmallowOrUp()) {
        this.isStreamMute(AudioManager.STREAM_MUSIC)
    } else {
        false
    }
}