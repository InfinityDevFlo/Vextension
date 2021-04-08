package eu.vironlab.vextension.document.storage


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.google.gson.JsonParser
import eu.vironlab.vextension.document.DefaultDocument
import eu.vironlab.vextension.document.Document
import eu.vironlab.vextension.document.DocumentManagement
import java.io.BufferedReader
import java.io.Reader
import java.io.Writer


class XMLDocumentStorage : DocumentStorage {

    private val xmlMapper: XmlMapper = XmlMapper()
    private val jsonMapper: ObjectMapper = ObjectMapper()

    override fun read(reader: Reader): DefaultDocument {
        BufferedReader(reader).use { bufferedReader ->
            return DocumentManagement.newDocument(
                JsonParser().parse(
                    jsonMapper.writeValueAsString(xmlMapper.readTree(bufferedReader)).toString()
                ).asJsonObject
            )
        }
    }

    override fun write(document: Document, writer: Writer) {
        throw UnsupportedOperationException("Writing a Document is not supportet with XML yet")
    }


}
