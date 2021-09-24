package com.zakaryan.myretailcrm.base.http.model.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.zakaryan.myretailcrm.base.http.model.data.Customer
import java.io.IOException

class CustomerDeserializer : JsonDeserializer<Customer>() {

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Customer {
        val treeNode = p.readValueAsTree<TreeNode>()
        val customerNode = treeNode.get("customer")
        return customerNode.traverse(p.codec).readValueAs(Customer::class.java)
    }
}