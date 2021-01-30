package io.benedictp.myblueprint.presentation.launches

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.util.Consumer
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.benedictp.domain.model.Launch
import io.benedictp.myblueprint.databinding.ItemLaunchBinding

class LaunchesAdapter(private val launchClickConsumer: Consumer<Launch>) : RecyclerView.Adapter<LaunchViewHolder>() {

	private val differCallback by lazy {
		object : DiffUtil.ItemCallback<Launch>() {
			override fun areItemsTheSame(oldItem: Launch, newItem: Launch): Boolean {
				return oldItem.id == newItem.id
			}

			override fun areContentsTheSame(oldItem: Launch, newItem: Launch): Boolean {
				return oldItem == newItem

			}
		}
	}

	private val asyncListDiffer = AsyncListDiffer(this, differCallback)

	init {
		setHasStableIds(true)
	}

	fun setData(newLaunches: List<Launch>) {
		asyncListDiffer.submitList(newLaunches)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchViewHolder {
		return LaunchViewHolder(ItemLaunchBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: LaunchViewHolder, position: Int) {
		holder.bindData(asyncListDiffer.currentList[position], launchClickConsumer)
	}

	override fun getItemCount(): Int {
		return asyncListDiffer.currentList.size
	}

	override fun getItemId(position: Int): Long {
		return asyncListDiffer.currentList[position].id.hashCode().toLong()
	}

}

class LaunchViewHolder(private val itemLaunchBinding: ItemLaunchBinding) : RecyclerView.ViewHolder(itemLaunchBinding.root) {

	fun bindData(launch: Launch, launchClickConsumer: Consumer<Launch>) {
		itemLaunchBinding.root.setOnClickListener { launchClickConsumer.accept(launch) }
		itemLaunchBinding.name.text = launch.name
	}

}