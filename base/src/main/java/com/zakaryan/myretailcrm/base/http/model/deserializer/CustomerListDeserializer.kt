package com.zakaryan.myretailcrm.base.http.model.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.zakaryan.myretailcrm.base.http.model.data.Customer
import java.io.IOException
import java.util.*

class CustomerListDeserializer : JsonDeserializer<List<Customer>>() {

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): List<Customer> {
        val result = ArrayList<Customer>()
        val treeNode = p.readValueAsTree<TreeNode>()
        val customersNode = treeNode.get("customers")
        val edgesNode = customersNode.get("edges")
        if (edgesNode.isArray) {
            for (i in 0 until edgesNode.size()) {
                val nodeWrapper = edgesNode.get(i)
                val node = nodeWrapper.get("node")
                val customer = node.traverse(p.codec).readValueAs(Customer::class.java)
                result.add(customer)
            }
        }
        return result
    }
}