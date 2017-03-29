package com.erikcupal.theblackcatclient.core

import io.socket.client.IO
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

private val trustAllCertificates = arrayOf<TrustManager>(object : X509TrustManager {
  override fun checkClientTrusted(p0: Array<java.security.cert.X509Certificate>, p1: String) { }
  override fun checkServerTrusted(p0: Array<java.security.cert.X509Certificate>, p1: String) { }
  override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> = arrayOf()
})

fun createSocketIoOptions(): IO.Options {
  val sslContext = SSLContext.getInstance("TLS")
  sslContext.init(null, trustAllCertificates, null)

  val options = IO.Options()
  options.reconnection = false
  options.sslContext = sslContext

  return options
}