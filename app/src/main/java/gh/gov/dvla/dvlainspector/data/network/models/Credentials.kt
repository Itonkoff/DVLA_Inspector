package gh.gov.dvla.dvlainspector.data.network.models

@kotlinx.serialization.Serializable
data class Credentials(var email: String, var password: String)