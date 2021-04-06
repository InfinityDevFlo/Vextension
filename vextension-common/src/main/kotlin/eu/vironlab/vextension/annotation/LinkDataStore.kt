package eu.vironlab.vextension.annotation

import eu.vironlab.vextension.database.data.DataStoreClient
import kotlin.reflect.KClass

annotation class LinkDataStore<T : DataStoreClient>(val store: KClass<T>)
