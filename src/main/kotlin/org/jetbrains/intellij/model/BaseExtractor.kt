package org.jetbrains.intellij.model

import com.jetbrains.plugin.structure.intellij.utils.JDOMUtil
import org.jdom2.Document
import org.jdom2.transform.JDOMSource
import org.jetbrains.intellij.transformXml
import java.io.File
import java.io.InputStream
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException
import javax.xml.transform.dom.DOMResult

abstract class BaseExtractor<T>(vararg classesToBeBound: Class<*>) {

    private val jaxbContext by lazy { JAXBContext.newInstance(*classesToBeBound) }

    @Throws(JAXBException::class)
    fun unmarshal(file: File) = unmarshal(file.inputStream())

    @Throws(JAXBException::class)
    fun unmarshal(inputStream: InputStream) = unmarshal(JDOMUtil.loadDocument(inputStream))

    @Suppress("UNCHECKED_CAST")
    @Throws(JAXBException::class)
    fun unmarshal(document: Document) = jaxbContext.createUnmarshaller().unmarshal(JDOMSource(document)) as T

    @Throws(JAXBException::class)
    fun marshal(bean: T, file: File) = DOMResult().apply {
        jaxbContext.createMarshaller().marshal(bean, this)
        transformXml(node, file)
    }
}
