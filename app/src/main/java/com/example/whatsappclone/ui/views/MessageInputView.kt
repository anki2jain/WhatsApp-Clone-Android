package com.example.whatsappclone.ui.views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.whatsappclone.databinding.ViewMessageInputBinding
import com.getstream.sdk.chat.interfaces.MessageSendListener
import com.getstream.sdk.chat.rest.Message
import com.getstream.sdk.chat.rest.interfaces.MessageCallback
import com.getstream.sdk.chat.rest.response.MessageResponse
import com.getstream.sdk.chat.view.MessageInputView
import com.getstream.sdk.chat.viewmodel.ChannelViewModel


/**
 * Basic message input view. Handles:
 * - Typing event
 * - Send message
 *
 * Doesn't handle more complex use cases like audio, video, file uploads, slash commands, editing text, threads or replies.
 * Stream's core API supports all of those though, so it's relatively easy to add
 *
 * When the user typed some text we change the microphone icon into a send button and hide the video button.
 */


class MessageInputView: ConstraintLayout, MessageSendListener
{
    val TAG =
        MessageInputView::class.java.simpleName

    private var binding: ViewMessageInputBinding? = null

    protected var viewModel: ChannelViewModel? = null


    constructor(context: Context) : super(context){
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet):    super(context, attrs){
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?,    defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    fun init(context: Context) {
        val inflater = LayoutInflater.from(context)
        binding = ViewMessageInputBinding.inflate(inflater, this, true)


    }

    fun getMessageText(): String? {
        return binding!!.messageInput.text.toString()
    }

    fun setViewModel(
        vmodel: ChannelViewModel,
        lifecycleOwner: LifecycleOwner?
    ) {
        viewModel = vmodel
        binding!!.lifecycleOwner = lifecycleOwner
        binding!!.viewModel = viewModel

        // implement message sending
        binding!!.voiceRecordingOrSend.setOnClickListener({
            var message : Message = Message()
            message.text = getMessageText()
            viewModel!!.sendMessage(message, object: MessageCallback {
                override fun onSuccess(response: MessageResponse?) {
                    // hi
                    viewModel!!.messageInputText.value = ""
                }

                override fun onError(errMsg: String?, errCode: Int) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            })
        })


        viewModel!!.messageInputText.observe(lifecycleOwner!!, Observer { text ->
            Log.i(TAG, "MessageInputText changed" + text)
        })


        // listen to typing events and connect to the view model
        binding!!.messageInput.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (s.toString().length > 0) viewModel!!.keystroke()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })
    }


    override fun onSendMessageError(errMsg: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSendMessageSuccess(message: Message?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}