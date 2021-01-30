package io.benedictp.myblueprint.presentation.launches

import androidx.recyclerview.widget.DiffUtil
import io.benedictp.domain.model.Launch

class LaunchesDiffUtil(private val oldList: List<Launch>, private val newList: List<Launch>) : DiffUtil.Callback() {

	override fun getOldListSize(): Int {
		return oldList.size
	}

	override fun getNewListSize(): Int {
		return newList.size
	}

	override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
		return oldList[oldItemPosition].id == newList[newItemPosition].id
	}

	override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
		return oldList[oldItemPosition] == newList[newItemPosition]
	}
}