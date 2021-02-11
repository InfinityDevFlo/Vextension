package eu.vironlab.vextension.document.storage

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.google.gson.JsonParser
import com.sun.corba.se.spi.ior.ObjectKey
import eu.vironlab.vextension.document.DefaultDocument
import eu.vironlab.vextension.document.Document
import java.io.Reader
import java.io.Writer


import eu.vironlab.vextension.document.DocumentManagement
import java.io.BufferedReader
import java.util.logging.Level.parse


class XMLDocumentStorage : DocumentStorage {

    private val xmlMapper: XmlMapper = XmlMapper()
    private val jsonMapper: ObjectMapper = ObjectMapper()

    override fun read(name: String, reader: Reader): DefaultDocument {
        BufferedReader(reader).use { bufferedReader ->
            return DocumentManagement.newDocument(name,
                JsonParser().parse(jsonMapper.writeValueAsString(xmlMapper.readTree(bufferedReader)).toString()).asJsonObject
            )
        }
    }

    override fun write(Document: Document, writer: Writer) {
        throw UnsupportedOperationException("Writing a Document is not supportet with XML yet")
    }


}
