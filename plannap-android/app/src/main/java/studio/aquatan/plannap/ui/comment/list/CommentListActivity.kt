package studio.aquatan.plannap.ui.comment.list

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import studio.aquatan.plannap.R
import studio.aquatan.plannap.databinding.ActivityCommentListBinding
import studio.aquatan.plannap.ui.ViewModelFactory
import studio.aquatan.plannap.ui.comment.CommentAdapter
import javax.inject.Inject

class CommentListActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_ID = "ID"
        private const val EXTRA_NAME = "NAME"

        fun createIntent(context: Context, planId: Long, planName: String) =
                Intent(context, CommentListActivity::class.java).apply {
                    putExtra(EXTRA_ID, planId)
                    putExtra(EXTRA_NAME, planName)
                }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: ActivityCommentListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_comment_list)

        AndroidInjection.inject(this)

        val planName = intent.getStringExtra(EXTRA_NAME)
        title = getString(R.string.title_comment_list_activity, planName)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val viewModel = ViewModelProvider(this, viewModelFactory).get(CommentListViewModel::class.java)
        binding.viewModel = viewModel

        val adapter = CommentAdapter(layoutInflater)
        binding.recyclerView.adapter = adapter

        viewModel.subscribe(adapter)
        viewModel.onActivityCreated(intent.getLongExtra(EXTRA_ID, 0))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    private fun CommentListViewModel.subscribe(adapter: CommentAdapter) {
        val activity = this@CommentListActivity

        commentList.observe(activity, Observer { adapter.submitList(it) })
    }
}
