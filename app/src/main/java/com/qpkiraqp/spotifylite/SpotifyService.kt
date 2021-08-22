package com.qpkiraqp.spotifylite

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.ImageUri
import com.spotify.protocol.types.Track
import com.android.volley.RequestQueue

import android.content.SharedPreferences
import com.qpkiraqp.spotifylite.model.entities.User
import android.R.string.no
import com.android.volley.toolbox.Volley


object SpotifyService {

    private const val CLIENT_ID = "4459fe889c8b4550943385bcc80e33df"
    private const val  REDIRECT_URI = "com.spotifylite://callback"

    private var spotifyAppRemote: SpotifyAppRemote? = null
    private var connectionParams: ConnectionParams = ConnectionParams.Builder(CLIENT_ID)
        .setRedirectUri(REDIRECT_URI)
        .showAuthView(true)
        .build()

    fun connect(context: Context, handler: (connected: Boolean) -> Unit) {
        if (spotifyAppRemote?.isConnected == true) {
            handler(true)
            return
        }
        val connectionListener = object : Connector.ConnectionListener {
            override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                this@SpotifyService.spotifyAppRemote = spotifyAppRemote
                handler(true)
            }
            override fun onFailure(throwable: Throwable) {
                Log.e("SpotifyService", throwable.message, throwable)
                handler(false)
            }
        }
        SpotifyAppRemote.connect(context, connectionParams, connectionListener)
    }

    enum class PlayingState {
        PAUSED, PLAYING, STOPPED
    }

    fun play(uri: String) {
            spotifyAppRemote?.playerApi?.play(uri)
        }

    fun resume() {
            spotifyAppRemote?.playerApi?.resume()
        }

    fun pause() {
            spotifyAppRemote?.playerApi?.pause()
        }

    fun playingState(handler: (PlayingState) -> Unit) {
            spotifyAppRemote?.playerApi?.playerState?.setResultCallback { result ->
                if (result.track.uri == null) {
                    handler(PlayingState.STOPPED)
                } else if (result.isPaused) {
                    handler(PlayingState.PAUSED)
                } else {
                    handler(PlayingState.PLAYING)
                }
            }
        }

    fun getCurrentTrack(handler: (track: Track) -> Unit) {
            spotifyAppRemote?.playerApi?.playerState?.setResultCallback { result ->
            handler(result.track)
            }
         }

    fun getImage(imageUri: ImageUri, handler: (Bitmap) -> Unit)  {
        spotifyAppRemote?.imagesApi?.getImage(imageUri)?.setResultCallback {
            handler(it)
             }
        }

    fun getCurrentTrackImage(handler: (Bitmap) -> Unit)  {
        getCurrentTrack {
            getImage(it.imageUri) {
                handler(it)
                }
            }
         }




    fun suscribeToChanges(handler: (Track) -> Unit) {
        spotifyAppRemote?.playerApi?.subscribeToPlayerState()?.setEventCallback {
            handler(it.track)
        }
    }

    fun getCategories(handler: (PlayingState) -> Unit){

        spotifyAppRemote?.userApi?.getLibraryState("äsdf");
    }



            fun disconnect() {
            SpotifyAppRemote.disconnect(spotifyAppRemote)
        }




}