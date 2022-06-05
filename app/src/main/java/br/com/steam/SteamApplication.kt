package br.com.steam

import android.app.Application
import br.com.steam.data.SteamDatabase

class SteamApplication: Application() {
    val steamDatabase: SteamDatabase by lazy{
        SteamDatabase.getInstance(this)
    }
}