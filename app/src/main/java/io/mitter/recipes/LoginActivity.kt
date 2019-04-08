package io.mitter.recipes

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.mitter.android.Mitter
import io.mitter.recipes.remote.ApiService
import io.mitter.recipes.remote.LoginRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var mitter: Mitter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val app = application as App
        apiService = app.apiService
        mitter = app.mitter

        johnLoginButton?.setOnClickListener {
            loginUser("@john")
        }

        amyLoginButton?.setOnClickListener {
            loginUser("@amy")
        }

        candiceLoginButton?.setOnClickListener {
            loginUser("@candice")
        }
    }

    private fun loginUser(username: String) {
        apiService.login(
            loginRequest = LoginRequest(
                username = username
            )
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { loginResponse, _ ->
                mitter.setUserId(loginResponse.userId)
                mitter.setUserAuthToken(loginResponse.userAuth)

                startActivity(Intent(this, MainActivity::class.java))
            }
    }
}
