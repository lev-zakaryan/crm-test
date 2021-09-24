package com.zakaryan.myretailcrm.ui.fragment.list.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zakaryan.myretailcrm.base.http.model.data.Customer
import com.zakaryan.myretailcrm.databinding.ListItemUserBinding
import java.util.*

class UserListAdapter(val listener: Listener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun interface Listener {
        fun openUserDetails(user: Customer)
    }

    private val _items: MutableList<Customer> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ListItemUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as UserViewHolder).bind(_items[position])
    }

    override fun getItemCount(): Int {
        return _items.size
    }

    fun setItems(items: List<Customer>) {
        _items.clear()
        _items.addAll(items)
        notifyDataSetChanged()
    }

    inner class UserViewHolder(private val binding: ListItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(Customer: Customer) {
            binding.firstName.text = Customer.firstName
            binding.root.setOnClickListener {
                listener.openUserDetails(Customer)
            }
        }
    }

}