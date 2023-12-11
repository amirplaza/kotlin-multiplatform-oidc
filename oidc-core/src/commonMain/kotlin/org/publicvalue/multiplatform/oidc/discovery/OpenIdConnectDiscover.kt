package org.publicvalue.multiplatform.oidc.discovery

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Url
import io.ktor.http.isSuccess
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.publicvalue.multiplatform.oidc.types.remote.OpenIdConnectConfiguration
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

/**
 * Basic OpenID Connect Discovery implementation.
 * For supported json keys, see [OpenIdConnectConfiguration].
 */
@OptIn(ExperimentalObjCName::class)
@ObjCName(swiftName = "OpenIdConnectDiscover", name = "OpenIdConnectDiscover", exact = true)
class OpenIdConnectDiscover(
    private val httpClient: HttpClient = HttpClient()
) {

    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    /**
     * Retrieve configuration document from url.
     *
     * @param configurationUrl the url
     * @return [OpenIdConnectConfiguration]
     */
    suspend fun downloadConfiguration(configurationUrl: String): OpenIdConnectConfiguration {
        return downloadConfiguration(Url(configurationUrl))
    }

    /**
     * Retrieve configuration document from url.
     *
     * @param configurationUrl the url
     * @return [OpenIdConnectConfiguration]
     */
    suspend fun downloadConfiguration(configurationUrl: Url): OpenIdConnectConfiguration {
        val result = httpClient.get(configurationUrl)
        val configuration: OpenIdConnectConfiguration = result.forceUnwrapBody(json)
        return configuration
    }
}

private suspend inline fun <reified T: Any> HttpResponse.forceUnwrapBody(json: Json = Json): T =
    if (call.response.status.isSuccess()) {
        val bodyString:String = call.body()
        json.decodeFromString(bodyString)
    } else {
        throw Exception("Could not download discovery document: $this")
    }