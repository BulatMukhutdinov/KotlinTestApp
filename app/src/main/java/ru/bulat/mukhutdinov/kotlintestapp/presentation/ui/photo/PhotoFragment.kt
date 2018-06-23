package ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photo

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.transition.TransitionInflater
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.photo.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import ru.bulat.mukhutdinov.kotlintestapp.R
import ru.bulat.mukhutdinov.kotlintestapp.presentation.bindable.PhotoBindable
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.MainActivity
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.MainRouter
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.base.BaseFragment
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photo.contract.PhotoPresenter
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photo.contract.PhotoView
import java.lang.Exception


class PhotoFragment : BaseFragment<PhotoPresenter, PhotoView, MainRouter>(), PhotoView, KodeinAware {

    companion object {
        private const val PHOTO = "extra"
        private const val TRANSITION_NAME = "transition_name"

        fun newInstance(photo: PhotoBindable, transitionName: String?): PhotoFragment {
            val fragment = PhotoFragment()
            val extras = Bundle()
            extras.putParcelable(PHOTO, photo)
            extras.putString(TRANSITION_NAME, transitionName)
            fragment.arguments = extras
            return fragment
        }
    }

    override val presenter: PhotoPresenter by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        postponeEnterTransition()
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.photo, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val photoBindable = arguments?.getParcelable(PHOTO) as PhotoBindable
        val transitionName = arguments?.getString(TRANSITION_NAME)

        photo.transitionName = transitionName

        Picasso.get()
                .load(photoBindable.url)
                .noFade()
                .into(photo, object : Callback {
                    override fun onSuccess() {
                        startPostponedEnterTransition()
                    }

                    override fun onError(e: Exception?) {
                        Toast.makeText(context, R.string.error, Toast.LENGTH_LONG).show()
                    }
                })
    }
}