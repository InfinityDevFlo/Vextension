package eu.vironlab.vextension.document.impl.storage


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.google.gson.JsonParser
import eu.vironlab.vextension.document.Document
import eu.vironlab.vextension.document.createDocument
import eu.vironlab.vextension.document.createDocumentFromJson
import eu.vironlab.vextension.document.impl.DefaultDocumentManagement
import eu.vironlab.vextension.document.storage.DocumentStorage
import java.io.BufferedReader
import java.io.Reader
import java.io.Writer


class XMLDocumentStorage : DocumentStorage {

    private val xmlMapper: XmlMapper = XmlMapper()
    private val jsonMapper: ObjectMapper = ObjectMapper()

    override fun <T> read(instance: T): Document {
        return createDocumentFromJson(DefaultDocumentManagement.GSON.toJson(instance))
    }

    override fun read(reader: Reader): Document {
        BufferedReader(reader).use { bufferedReader ->
            return createDocument(
                JsonParser().parse(
                    jsonMapper.writeValueAsString(xmlMapper.readTree(bufferedReader)).toString()
                ).asJsonObject
            )
        }
    }

    override fun <T> write(document: Document, clazz: Class<T>): T {
        return DefaultDocumentManagement.GSON.fromJson(document.toJson(), clazz)
    }

    override fun write(document: Document, writer: Writer) {
        this.xmlMapper.writeValue(writer, document.toPlainObjects())
    }


}
