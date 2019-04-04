package com.example.jean.jcplayer.view

import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.content.res.TypedArray
import android.graphics.PorterDuff
import android.media.AudioManager
import android.os.Build
import android.os.CountDownTimer
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.jean.jcplayer.JcPlayerManager
import com.example.jean.jcplayer.JcPlayerManagerListener
import com.example.jean.jcplayer.R
import com.example.jean.jcplayer.Utils
import com.example.jean.jcplayer.general.JcStatus
import com.example.jean.jcplayer.general.errors.AudioListNullPointerException
import com.example.jean.jcplayer.general.errors.OnInvalidPathListener
import com.example.jean.jcplayer.model.JcAudio
import kotlinx.android.synthetic.main.new_jc_player.view.*
import uk.co.chrisjenx.calligraphy.CalligraphyConfig


/**
 * This class is the JcAudio View. Handles user interactions and communicate with [JcPlayerManager].
 * @author Jean Carlos (Github: @jeancsanchez)
 * @date 12/07/16.
 * Jesus loves you.
 */
class JcPlayerView : LinearLayout, View.OnClickListener, SeekBar.OnSeekBarChangeListener, JcPlayerManagerListener, View.OnTouchListener, AdapterView.OnItemSelectedListener {


    /***
     * The media player is for radio play.
     */
    private var _xDelta: Int = 0
    private var _yDelta: Int = 0
    internal var windowwidth: Int = 0 // Actually the width of the RelativeLayout.
    internal var windowheight: Int = 0 // Actually the height of the RelativeLayout.
    private var isOutReported = false
    var jcbuttonclicked: JcButtonClicked? = null
    var onMediaLoad: onMediaLoaded ? = null


    var jcbuttonPlayclicked: JcButtonPlayClicked? = null
    var jcbuttonpauseclicked: JcButtonPlayClicked? = null


    var station = arrayOf("Heart London ", "BBC Radio 1 ", "BBC Radio 2 ", "BBC Radio 4 ", "Capital FM ")

    private var clicked: Int = 0

    val stationArrayList = ArrayList<String>()//Creating an empty arraylist

    var isMute: Boolean? = null

    var isFirstPlay: Boolean = true

    var jcListener: JcPlayerButtonClickListener? = null



    interface JcButtonClicked {
        fun buttonClicked()
    }


    interface JcButtonPlayClicked {
        fun buttonPlayClicked()
        fun buttonPauseClicked()

    }



    interface onMediaLoaded {
        fun onYoutubeLoaded(): Boolean
    }


    fun onYoutubeLoaded(onMediaLoaded : onMediaLoaded){
        onMediaLoad = onMediaLoaded
    }


    fun onClickAction(jcButtonClicked: JcButtonClicked) {
        jcbuttonclicked = jcButtonClicked
    }




    fun onJcClickPLay(JcButtonPlayClicked: JcButtonPlayClicked) {
        jcbuttonPlayclicked = JcButtonPlayClicked
    }

    fun onJcClickPause(JcButtonPauseClicked: JcButtonPlayClicked) {
        jcbuttonpauseclicked = JcButtonPauseClicked
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        val X = event?.rawX?.toInt()
        val Y = event?.rawY?.toInt()

        if (isOut(view!!)) {
            if (!isOutReported) {
                isOutReported = true
                //Toast.makeText(this, "OUT", Toast.LENGTH_SHORT).show();
            }
        } else {
            isOutReported = false
        }

        when (event?.getAction()!! and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {

                Toast.makeText(context, "Screen get Touched down", Toast.LENGTH_SHORT).show()
                // _xDelta and _yDelta record how far inside the view we have touched. These
                // values are used to compute new margins when the view is moved.
                _xDelta = X!! - view.getLeft()
                _yDelta = Y!! - view.getTop()

            }
            MotionEvent.ACTION_UP -> {

                Toast.makeText(context, "Screen get Touched", Toast.LENGTH_SHORT).show()

            }
            MotionEvent.ACTION_MOVE -> {
//                val lp = view.layoutParams as FrameLayout.LayoutParams
                val lp = view.layoutParams as LinearLayout.LayoutParams
                // Image is centered to start, but we need to unhitch it to move it around.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                    lp.removeRule(RelativeLayout.CENTER_HORIZONTAL)
//                    lp.removeRule(RelativeLayout.CENTER_VERTICAL)
                } else {
//                    lp.addRule(RelativeLayout.CENTER_HORIZONTAL, 0)
//                    lp.addRule(RelativeLayout.CENTER_VERTICAL, 0)
                }


//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                },3000);

                lp.leftMargin = X!! - _xDelta
                lp.topMargin = Y!! - _yDelta
                // Negative margins here ensure that we can move off the screen to the right
                // and on the bottom. Comment these lines out and you will see that
                // the image will be hemmed in on the right and bottom and will actually shrink.
                lp.rightMargin = view.getWidth() - lp.leftMargin - windowwidth
                lp.bottomMargin = view.getHeight() - lp.topMargin - windowheight
                view.setLayoutParams(lp)
            }
        }

        return true
    }

    private fun isOut(view: View): Boolean {
        // Check to see if the view is out of bounds by calculating how many pixels
        // of the view must be out of bounds to and checking that at least that many
        // pixels are out.
        val percentageOut = 0.50f
        val viewPctWidth = (view.width * percentageOut).toInt()
        val viewPctHeight = (view.height * percentageOut).toInt()

        return -view.left >= viewPctWidth ||
                view.right - windowwidth > viewPctWidth ||
                -view.top >= viewPctHeight ||
                view.bottom - windowheight > viewPctHeight
    }


    private val jcPlayerManager: JcPlayerManager by lazy {
        JcPlayerManager.getInstance(context).get()!!
    }


    val jcAudios = java.util.ArrayList<JcAudio>()

    val obj_adapter = null


    val spinAdapter = null

    val handler2 = Handler()

    var isListOpen = false;
    var isPlayerClicked: Boolean = false


    val myPlaylist: List<JcAudio>?
        get() = jcPlayerManager.playlist

    val isPlaying: Boolean
        get() = jcPlayerManager.isPlaying()

    val isPaused: Boolean
        get() = jcPlayerManager.isPaused()

    val currentAudio: JcAudio?
        get() = jcPlayerManager.currentAudio

    var onInvalidPathListener: OnInvalidPathListener? = null

    var jcPlayerManagerListener: JcPlayerManagerListener? = null
        set(value) {
            jcPlayerManager.jcPlayerManagerListener = value
        }

    lateinit var audioManager: AudioManager

    internal var mMaxVolume: Int = 0
    internal var current_volume: Int = 0

    internal var playState: Int = 0

    internal var increaseinSound = 0
    val utils = Utils(context)


    companion object {
        private const val PULSE_ANIMATION_DURATION = 200L
        private const val TITLE_ANIMATION_DURATION = 600
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()

        context.theme
                .obtainStyledAttributes(attrs, R.styleable.JcPlayerView, 0, 0)
                .also { setAttributes(it) }
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()

        context.theme
                .obtainStyledAttributes(attrs, R.styleable.JcPlayerView, defStyle, 0)
                .also { setAttributes(it) }
    }


    public fun init() {
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Montserrat-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )
        View.inflate(context, R.layout.new_jc_player, this)
        btnNext?.setOnClickListener(this)
        btnPrev?.setOnClickListener(this)
        btnPlay?.setOnClickListener(this)
        btnPause?.setOnClickListener(this)
        btnRepeat?.setOnClickListener(this)
        btnVolLow?.setOnClickListener(this)
        btnVolHigh?.setOnClickListener(this)
        llNext?.setOnClickListener(this)
        llPrev?.setOnClickListener(this)
        llVolLow?.setOnClickListener(this)
        llVolHigh?.setOnClickListener(this)
        //imageDrop?.setOnClickListener(this)
        //btn_header?.setOnClickListener(this)
        //small_radio_txt_min?.setOnClickListener(this)
        minRadioBtnLowJc?.setOnClickListener(this)
        minRadioBtnhighJc?.setOnClickListener(this)
        //spinner.setOnClickListener(this)
//        txtCurrentMusic.text =""
        //getTimer()
        //backView?.setOnClickListener(this)
//        jcMediaPlayer?.setOnTouchListener(this)


        btnRepeatOne?.setOnClickListener(this)
        seekBar?.setOnSeekBarChangeListener(this)
        //txtCurrentMusic?.setOnClickListener(this)
        //txtCurrentNow?.setOnClickListener(this)
        audioManager = this.context.getSystemService(AUDIO_SERVICE) as AudioManager
        mMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        //recyclerList.layoutManager = LinearLayoutManager(this.context)


        spinner.adapter = ArrayAdapter(context, R.layout.radio_station_list, station)
        //spinner.adapter = MyStationListAdapter(context, R.layout.radio_station_list, station)

//        txtCurrentNow.setTypeface(utils._montserrat_bold)


        spinner.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    jcbuttonclicked?.buttonClicked()
                    spinner.setOnItemSelectedListener(this)
                    val handler = Handler()
                    handler.postDelayed({
                        hideSpinnerDropDown(spinner)
                    }, 10000)
                }
                MotionEvent.ACTION_UP -> {
//                    jcbuttonclicked?.buttonClicked()
//                    spinner.c(this)
                }
            }
            return@OnTouchListener false
        })

        //txtCurrentMusic.setTypeface(null, Typeface.BOLD_ITALIC);


        /***
         * Channels for radio play
         */

        jcAudios.add(JcAudio.createFromURL("Heart London", "http://media-ice.musicradio.com/HeartLondonMP3"))
        jcAudios.add(JcAudio.createFromURL("BBC Radio 1", "http://bbcmedia.ic.llnwd.net/stream/bbcmedia_radio1_mf_p"))
        jcAudios.add(JcAudio.createFromURL("BBC Radio 2", "http://bbcmedia.ic.llnwd.net/stream/bbcmedia_radio2_mf_p"))
        jcAudios.add(JcAudio.createFromURL("BBC Radio 4", "http://bbcmedia.ic.llnwd.net/stream/bbcmedia_radio4fm_mf_p"))
        jcAudios.add(JcAudio.createFromURL("Capital FM", "http://media-the.musicradio.com/CapitalMP3"))
        initPlaylist(jcAudios, null)

//        var b = minRadioLayoutMin.visibility

//        val handler = Handler()
//        handler.postDelayed({
//
//            minRadioLayoutMin.visibility = View.VISIBLE
//            frameLayout.visibility = View.GONE
//
//        }, 5000)
        frameLayout.visibility = View.VISIBLE

    }

    private fun setAttributes(attrs: TypedArray) {
        val defaultColor = ResourcesCompat.getColor(resources, android.R.color.white, null)
        txtCurrentMusic?.setTextColor(attrs.getColor(R.styleable.JcPlayerView_text_audio_title_color, defaultColor))
//        txtCurrentNow?.setTextColor(attrs.getColor(R.styleable.JcPlayerView_text_audio_title_color, defaultColor))
        txtCurrentDuration?.setTextColor(attrs.getColor(R.styleable.JcPlayerView_text_audio_current_duration_color, defaultColor))
        txtDuration?.setTextColor(attrs.getColor(R.styleable.JcPlayerView_text_audio_duration_color, defaultColor))

        progressBarPlayer?.indeterminateDrawable?.setColorFilter(attrs.getColor(R.styleable.JcPlayerView_progress_color, defaultColor), PorterDuff.Mode.SRC_ATOP)
        seekBar?.progressDrawable?.setColorFilter(attrs.getColor(R.styleable.JcPlayerView_seek_bar_color, defaultColor), PorterDuff.Mode.SRC_ATOP)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            seekBar?.thumb?.setColorFilter(attrs.getColor(R.styleable.JcPlayerView_seek_bar_color, defaultColor), PorterDuff.Mode.SRC_ATOP)
            // TODO: change thumb color in older versions (14 and 15).
        }

        btnPlay.setColorFilter(attrs.getColor(R.styleable.JcPlayerView_play_icon_color, defaultColor))
        btnPlay.setImageResource(attrs.getResourceId(R.styleable.JcPlayerView_play_icon, R.drawable.play))

        btnPause.setImageResource(attrs.getResourceId(R.styleable.JcPlayerView_pause_icon, R.drawable.pause))
        btnPause.setColorFilter(attrs.getColor(R.styleable.JcPlayerView_pause_icon_color, defaultColor))

        btnNext?.setColorFilter(attrs.getColor(R.styleable.JcPlayerView_next_icon_color, defaultColor))
        btnNext?.setImageResource(attrs.getResourceId(R.styleable.JcPlayerView_next_icon, R.drawable.next))

        btnPrev?.setColorFilter(attrs.getColor(R.styleable.JcPlayerView_previous_icon_color, defaultColor))
        btnPrev?.setImageResource(attrs.getResourceId(R.styleable.JcPlayerView_previous_icon, R.drawable.prev))

//        btnVolHigh?.setColorFilter(attrs.getColor(R.styleable.JcPlayerView_previous_icon_color, defaultColor))
        btnVolHigh?.setImageResource(attrs.getResourceId(R.styleable.JcPlayerView_previous_icon, R.drawable.vol_add_new))

//        btnVolLow?.setColorFilter(attrs.getColor(R.styleable.JcPlayerView_previous_icon_color, defaultColor))
        if (current_volume == 0) {
            btnVolLow?.setImageResource(attrs.getResourceId(R.styleable.JcPlayerView_previous_icon, R.drawable.volsuperlow))
        } else {
            btnVolLow?.setImageResource(attrs.getResourceId(R.styleable.JcPlayerView_previous_icon, R.drawable.vol_minus_new))
        }



        if (current_volume == 15) {
            btnVolHigh?.setImageResource(attrs.getResourceId(R.styleable.JcPlayerView_previous_icon, R.drawable.volsuper))
        } else {
            btnVolHigh?.setImageResource(attrs.getResourceId(R.styleable.JcPlayerView_previous_icon, R.drawable.vol_add_new))
        }


//        btnRandom?.setColorFilter(attrs.getColor(R.styleable.JcPlayerView_random_icon_color, defaultColor))
//        btnRandomIndicator?.setColorFilter(attrs.getColor(R.styleable.JcPlayerView_random_icon_color, defaultColor))
//        btnRandom?.setImageResource(attrs.getResourceId(R.styleable.JcPlayerView_random_icon, R.drawable.ic_shuffle))
//        attrs.getBoolean(R.styleable.JcPlayerView_show_random_button, true).also { showButton ->
//            if (showButton) {
//                btnRandom?.visibility = View.GONE
//            } else {
//                btnRandom?.visibility = View.GONE
//            }
//        }

//        btnRepeat?.setColorFilter(attrs.getColor(R.styleable.JcPlayerView_repeat_icon_color, defaultColor))
//        btnRepeatIndicator?.setColorFilter(attrs.getColor(R.styleable.JcPlayerView_repeat_icon_color, defaultColor))
//        btnRepeat?.setImageResource(attrs.getResourceId(R.styleable.JcPlayerView_repeat_icon, R.drawable.ic_repeat))
//        attrs.getBoolean(R.styleable.JcPlayerView_show_repeat_button, true).also { showButton ->
//            if (showButton) {
//                btnRepeat?.visibility = View.GONE
//            } else {
//                btnRepeat?.visibility = View.GONE
//            }
//        }
//
//        btnRepeatOne?.setColorFilter(attrs.getColor(R.styleable.JcPlayerView_repeat_one_icon_color, attrs.getColor(R.styleable.JcPlayerView_repeat_icon_color, defaultColor)))
//        btnRepeatOne?.setImageResource(attrs.getResourceId(R.styleable.JcPlayerView_repeat_one_icon, R.drawable.ic_repeat_one))
    }

    /**
     * Initialize the playlist and controls.
     *
     * @param playlist List of JcAudio objects that you want play
     * @param jcPlayerManagerListener The view status jcPlayerManagerListener (optional)
     */
    fun initPlaylist(playlist: List<JcAudio>, jcPlayerManagerListener: JcPlayerManagerListener? = null) {
        /*Don't sort if the playlist have position number.
        We need to do this because there is a possibility that the user reload previous playlist
        from persistence storage like sharedPreference or SQLite.*/
        if (isAlreadySorted(playlist).not()) {
            sortPlaylist(playlist)
        }

        jcPlayerManager.playlist = playlist as ArrayList<JcAudio>
        jcPlayerManager.jcPlayerManagerListener = jcPlayerManagerListener
        jcPlayerManager.jcPlayerManagerListener = this
    }

    /**
     * Initialize an anonymous playlist with a default JcPlayer title for all audios
     *
     * @param playlist List of urls strings
     */
    fun initAnonPlaylist(playlist: List<JcAudio>) {
        generateTitleAudio(playlist, context.getString(R.string.track_number))
        initPlaylist(playlist)
    }

    /**
     * Initialize an anonymous playlist, but with a custom title for all audios
     *
     * @param playlist List of JcAudio files.
     * @param title    Default title for all audios
     */
    fun initWithTitlePlaylist(playlist: List<JcAudio>, title: String) {
        generateTitleAudio(playlist, title)
        initPlaylist(playlist)
    }

    /**
     * Add an audio for the playlist. We can track the JcAudio by
     * its id. So here we returning its id after adding to list.
     *
     * @param jcAudio audio file generated from [JcAudio]
     * @return jcAudio position.
     */
    fun addAudio(jcAudio: JcAudio): Int {
        jcPlayerManager.playlist.let {
            val lastPosition = it.size
            jcAudio.position = lastPosition + 1

            if (it.contains(jcAudio).not()) {
                it.add(lastPosition, jcAudio)
            }

            return jcAudio.position!!
        }
    }

    /**
     * Remove an audio for the playlist
     *
     * @param jcAudio JcAudio object
     */
    fun removeAudio(jcAudio: JcAudio) {
        jcPlayerManager.playlist.let {
            if (it.contains(jcAudio)) {
                if (it.size > 1) {
                    // play next audio when currently played audio is removed.
                    if (jcPlayerManager.isPlaying()) {
                        if (jcPlayerManager.currentAudio == jcAudio) {
                            it.remove(jcAudio)
                            pause()
                            resetPlayerInfo()
                        } else {
                            it.remove(jcAudio)
                        }
                    } else {
                        it.remove(jcAudio)
                    }
                } else {
                    //TODO: Maybe we need jcPlayerManager.stopPlay() for stopping the player
                    it.remove(jcAudio)
                    pause()
                    resetPlayerInfo()
                }
            }
        }
    }

    /**
     * Plays the give audio.
     * @param jcAudio The audio to be played.
     */
    fun playAudio(jcAudio: JcAudio) {


        showProgressBar()

        jcPlayerManager.playlist.let {
            if (it.contains(jcAudio).not()) {
                it.add(jcAudio)
            }
//            txtCurrentNow.visibility = View.VISIBLE
            var station = spinner.selectedItem.toString();
//            txtCurrentNow.text = station
            jcPlayerManager.playAudio(jcAudio)
        }
    }

    /**
     * Shows the play button on player.
     */
    private fun showPlayButton() {
        btnPlay?.visibility = ImageView.VISIBLE
        btnPause?.visibility = ImageView.GONE
    }

    /**
     * Shows the pause button on player.
     */
    private fun showPauseButton() {
        btnPlay?.visibility = ImageView.GONE
        btnPause?.visibility = ImageView.VISIBLE
    }

    /**
     * Goes to next audio.
     */
    fun next() {
        jcPlayerManager.let { player ->
            player.currentAudio?.let {
                resetPlayerInfo()
                showProgressBar()

                try {
                    player.nextAudio()
                } catch (e: AudioListNullPointerException) {
                    dismissProgressBar()
                    e.printStackTrace()
                }
            }
        }
    }

    private var volLevel: Boolean = false


    /**
     * Continues the current audio.
     */
    fun continueAudio() {
        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        if (current_volume == 0) {
            btnVolLow?.setImageResource(R.drawable.volsuperlow)
        } else {
            btnVolLow?.setImageResource(R.drawable.vol_minus_new)
        }

        if (current_volume == 15) {
            btnVolHigh?.setImageResource(R.drawable.volsuper)
        } else {
            btnVolHigh?.setImageResource(R.drawable.vol_add_new)
        }

        showProgressBar()

        try {
            jcPlayerManager.continueAudio()
        } catch (e: AudioListNullPointerException) {
            dismissProgressBar()
            e.printStackTrace()
        }
    }

    /**
     * Pauses the current audio.
     */
    fun pause() {
        jcPlayerManager.pauseAudio()
        showPlayButton()
    }


    /**
     * Goes to previous audio.
     */
    fun previous() {
        resetPlayerInfo()
        showProgressBar()

        try {
            jcPlayerManager.previousAudio()
        } catch (e: AudioListNullPointerException) {
            dismissProgressBar()
            e.printStackTrace()
        }

    }


    private var waitTimer: CountDownTimer? = null

    fun getTimer() {

        if (waitTimer != null) {
            waitTimer!!.cancel()
            waitTimer = null
        }
        waitTimer = object : CountDownTimer(8000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                //called every 300 milliseconds, which could be used to
                //send messages or some other action
            }

            override fun onFinish() {
                //minRadioLayoutMin.visibility = View.VISIBLE
                frameLayout.visibility = View.GONE
            }
        }.start()

    }


    override fun onClick(view: View) {
        jcbuttonclicked?.buttonClicked()

//        setBackground(ContextCompat.getDrawable(context, R.drawable.drawable_pressed))
//        Handler().postDelayed({
//            setBackground(ContextCompat.getDrawable(context, R.drawable.transparent_white_media))
//        }, 8000)

//        jListener.onJcPlayerButtonClick()


        when (view.id) {
            R.id.btnPlay ->
                btnPlay?.let {
                    if (utils.isNetworkConnected) {
                        playState = 1
                        jcbuttonPlayclicked?.buttonPlayClicked()
                        applyPulseAnimation(it)

//                        txtCurrentNow.visibility = View.VISIBLE
                        if (isFirstPlay) {
                            isFirstPlay = false
                            spinner.visibility = Spinner.VISIBLE
                            spinner.setOnItemSelectedListener(this)
                            continueAudio()

                        } else {

                            continueAudio()
                        }
                    } else {
//                        isFirstPlay = true
                        pause()
                    }


                    //getTimer()
                }

            R.id.btnPause -> {
                btnPause?.let {

                    jcbuttonpauseclicked?.buttonPauseClicked()
                    applyPulseAnimation(it)
                    pause()
                    //getTimer()
                }
            }

            R.id.btnNext ->
                btnNext?.let {
                    if (utils.isNetworkConnected) {
                        if (playState == 1) {
//                        txtCurrentNow.visibility = View.VISIBLE
                            applyPulseAnimation(it)
                            changeSpinnerInc()
                            next()
                        }
                    } else {
                        pause()
                    }

                    //getTimer()
                }


            R.id.btnPrev ->
                btnPrev?.let {
                    if (utils.isNetworkConnected) {
                        if (playState == 1) {
//                            txtCurrentNow.visibility = View.VISIBLE
                            applyPulseAnimation(it)
                            previous()
                            changeSpinnerDec()
                        }
                    } else {
                        pause()
                    }

                    //getTimer()
                }

//            R.id.btnRandom -> {
//                jcPlayerManager.onShuffleMode = jcPlayerManager.onShuffleMode.not()
//                //btnRandomIndicator.visibility = if (jcPlayerManager.onShuffleMode) View.VISIBLE else View.GONE
//            }

            R.id.btn_header ->
                btn_header?.let {
                    if (utils.isNetworkConnected) {
                        playMusic()

                    } else {
                        pause()
                    }
                    //getTimer()
                }


            R.id.btnVolHigh -> {
                btnVolHigh?.let {

                    volumeHigh()

//                    if(volLevel){
//                        btnVolHigh?.setImageResource(R.drawable.vol_add_new)
//                    }
//                    else{
//                        btnVolHigh?.setImageResource(R.drawable.volsuper)
//                    }
                    //getTimer()
                }
            }


            R.id.llVolHigh -> {
                llVolHigh?.let {

                    volumeHigh()

//                    if(volLevel){
//                        btnVolHigh?.setImageResource(R.drawable.vol_add_new)
//                    }
//                    else{
//                        btnVolHigh?.setImageResource(R.drawable.volsuper)
//                    }


                    //getTimer()
                }

            }

            R.id.btnVolLow -> {
                btnVolLow?.let {


                    volumeLow()
//                    if(volLevel){
//                        btnVolLow?.setImageResource(R.drawable.vol_minus_new)
//                    }else{
//                        volLevel= true
//                        btnVolLow?.setImageResource(R.drawable.volsuperlow)
//                    }
                    //getTimer()

                }
            }

            R.id.llVolLow -> {
                btnVolLow?.let {

                    //                    if(volLevel){
//                        btnVolLow?.setImageResource(R.drawable.vol_minus_new)
//                    }else{
//                        volLevel = true
//                        btnVolLow?.setImageResource(R.drawable.volsuperlow)
//                    }
                    volumeLow()
                    //getTimer()
                }
            }

//            R.id.small_radio_txt_min -> {
//                minRadioLayoutMin.visibility = View.GONE
//                frameLayout.visibility = View.VISIBLE
//                //getTimer()
//            }

            R.id.minRadioBtnhighJc -> {
                volumeHigh()

            }

            R.id.minRadioBtnLowJc -> {
                volumeLow()
            }

            R.id.spinner -> {
                jcbuttonclicked?.buttonClicked()
            }

//            R.id.imageDrop ->{
//                imageDrop?.let {
//                    playMusic()
//                }
//            }
//            R.id.backView ->{
//                backView?.let {
//                    backView.setBackground(ContextCompat.getDrawable(context, R.drawable.drawable_pressed));
//
//                }
//            }


        }
    }

    /**
     * Create a notification player with same playlist with a custom icon.
     *
     * @param iconResource icon path.
     */
    fun createNotification(iconResource: Int) {
        jcPlayerManager.createNewNotification(iconResource)
    }

    /**
     * Create a notification player with same playlist with a default icon
     */
    fun createNotification() {
        jcPlayerManager.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // For light theme
                it.createNewNotification(R.drawable.ic_notification_default_black)
            } else {
                // For dark theme
                it.createNewNotification(R.drawable.ic_notification_default_white)
            }
        }
    }

    override fun onPreparedAudio(status: JcStatus) {
        dismissProgressBar()
        resetPlayerInfo()
        onUpdateTitle(status.jcAudio.title)

        val duration = status.duration.toInt()
        seekBar?.post { seekBar?.max = duration }
        //txtDuration?.post { txtDuration?.text = toTimeSongString(duration) }
    }

    override fun onProgressChanged(seekBar: SeekBar, i: Int, fromUser: Boolean) {
        jcPlayerManager.let {
            if (fromUser) {
                it.seekTo(i)
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        if (jcPlayerManager.currentAudio != null) {
            showProgressBar()
        }
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        dismissProgressBar()

        if (jcPlayerManager.isPaused()) {
            showPlayButton()
        }
    }

    override fun onCompletedAudio() {
        resetPlayerInfo()
        try {
            continueAudio()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onContinueAudio(status: JcStatus) {
        dismissProgressBar()
    }

    override fun onPlaying(status: JcStatus) {
        dismissProgressBar()
        showPauseButton()
    }

    override fun onTimeChanged(status: JcStatus) {
        var status2 =status
        Log.d("Tag", "sdsd"+status2)


        val currentPosition = status.currentPosition.toInt()
        seekBar?.post { seekBar?.progress = currentPosition }
        //txtCurrentDuration?.post { txtCurrentDuration?.text = toTimeSongString(currentPosition) }
    }

    override fun onPaused(status: JcStatus) {
    }

    override fun onStopped(status: JcStatus) {
    }

    override fun onJcpError(throwable: Throwable) {
        // TODO
//        jcPlayerManager.currentAudio?.let {
//            onInvalidPathListener?.onPathError(it)
//        }
    }

    private fun showProgressBar() {
        progressBarPlayer?.visibility = ProgressBar.VISIBLE
        btnPlay?.visibility = ImageView.GONE
        btnPause?.visibility = ImageView.GONE
    }

    private fun dismissProgressBar() {
        progressBarPlayer?.visibility = ProgressBar.GONE
        showPauseButton()
    }

    private fun onUpdateTitle(title: String) {
        //txtCurrentMusic.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        //imageDrop.visibility = View.VISIBLE

//        txtCurrentMusic?.let {
//            it.visibility = View.VISIBLE
//            YoYo.with(Techniques.FadeInLeft)
//                    .duration(TITLE_ANIMATION_DURATION.toLong())
//                    .playOn(it)
//
//            it.post {
//                it.text = title
//            }
//        }
    }

    private fun resetPlayerInfo() {
        //txtCurrentMusic.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        imageDrop.visibility = View.GONE
        //txtCurrentMusic?.post { txtCurrentMusic.text = "" }
        seekBar?.post { seekBar?.progress = 0 }
        //txtDuration?.post { txtDuration.text = context.getString(R.string.play_initial_time) }
        //txtCurrentDuration?.post { txtCurrentDuration.text = context.getString(R.string.play_initial_time) }
    }

    /**
     * Sorts the playlist.
     */
    private fun sortPlaylist(playlist: List<JcAudio>) {
        for (i in playlist.indices) {
            val jcAudio = playlist[i]
            jcAudio.position = i
        }
    }

    /**
     * Check if playlist already sorted or not.
     * We need to check because there is a possibility that the user reload previous playlist
     * from persistence storage like sharedPreference or SQLite.
     *
     * @param playlist list of JcAudio
     * @return true if sorted, false if not.
     */
    private fun isAlreadySorted(playlist: List<JcAudio>?): Boolean {
        // If there is position in the first audio, then playlist is already sorted.
        return playlist?.let { it[0].position != -1 } == true
    }

    /**
     * Generates a default audio title for each audio on list.
     * @param playlist The audio list.
     * @param title The default title.
     */
    private fun generateTitleAudio(playlist: List<JcAudio>, title: String) {
        for (i in playlist.indices) {
            if (title == context.getString(R.string.track_number)) {
                playlist[i].title = context.getString(R.string.track_number) + " " + (i + 1).toString()
            } else {
                playlist[i].title = title
            }
        }
    }

    private fun applyPulseAnimation(view: View?) {
        view?.postDelayed({
            YoYo.with(Techniques.Pulse)
                    .duration(PULSE_ANIMATION_DURATION)
                    .playOn(view)
        }, PULSE_ANIMATION_DURATION)
    }

    /**
     * Kills the player
     */
    fun kill() {
        jcPlayerManager.kill()
    }


    open interface BtnClickListener {
        fun onBtnClick(position: Int)
    }




    fun hidePlayer() {
        //txtCurrentMusic?.visibility = TextView.GONE
        txtCurrentDuration?.visibility = TextView.GONE
        txtDuration?.visibility = TextView.GONE
        btnPrev?.visibility = TextView.GONE
        btnNext?.visibility = TextView.GONE
        progressBarPlayer?.visibility = ProgressBar.GONE
        btnPlay?.visibility = TextView.GONE
        btnPause?.visibility = TextView.GONE
        seekBar?.visibility = SeekBar.GONE
        btnVolHigh.visibility = ImageView.GONE
        btnVolLow.visibility = ImageView.GONE
    }


    fun showPlayer() {
        //txtCurrentMusic?.visibility = TextView.VISIBLE
        txtCurrentDuration?.visibility = TextView.VISIBLE
        txtDuration?.visibility = TextView.VISIBLE
        btnPrev?.visibility = TextView.VISIBLE
        btnNext?.visibility = TextView.VISIBLE
        if (jcPlayerManager.isPlaying()) {
            btnPause?.visibility = ImageView.GONE
            btnPlay?.visibility = ImageView.GONE
            progressBarPlayer?.visibility = TextView.VISIBLE
        } else {
            btnPlay?.visibility = ImageView.GONE
            btnPause?.visibility = TextView.GONE
        }
        seekBar?.visibility = SeekBar.GONE
        btnVolHigh.visibility = ImageView.VISIBLE
        btnVolLow.visibility = ImageView.VISIBLE

    }

    fun volumeLow() {

        val decrease4dec = 4
        val decrease3dec = 3
        val decrease2dec = 2
        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        if (current_volume > 13 && current_volume <= 15) {
            btnVolHigh?.setImageResource(R.drawable.vol_add_new)

            increaseinSound = current_volume - decrease2dec
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, increaseinSound, 0)
            current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        } else if (current_volume <= 13 && current_volume > 10) {
            increaseinSound = current_volume - decrease3dec
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, increaseinSound, 0)
            current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        } else if (current_volume <= 10 && current_volume > 6) {
            increaseinSound = current_volume - decrease4dec
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, increaseinSound, 0)
            current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        } else if (current_volume > 2 && current_volume <= 6) {
            increaseinSound = current_volume - decrease4dec
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, increaseinSound, 0)
            current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        } else {
            increaseinSound = current_volume - decrease2dec
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, increaseinSound, 0)
            current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            if (current_volume == 0) {
                btnVolLow?.setImageResource(R.drawable.volsuperlow)
                btnVolHigh?.setImageResource(R.drawable.vol_add_new)
            }
        }
        //Toast.makeText(context, current_volume.toString(), Toast.LENGTH_SHORT).show()

//                else if(current_volume == 0){
//                    btnVolLow?.setImageDrawable(resources.getDrawable(R.drawable.vol_down))
//                    increaseinSound = current_volume + decrease8dec
//                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, increaseinSound, 0)
//                    current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
//                }

    }

    fun volumeHigh() {
//        val increase6dec = 6
        val increase4dec = 4
        val increase3dec = 3
        val increase2dec = 2

        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        if (current_volume > 1 && current_volume < 6) run {
            btnVolLow?.setImageResource(R.drawable.vol_minus_new)

            increaseinSound = current_volume + increase4dec
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, increaseinSound, 0)
            current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            //handle_volume_btn(current_volume);
        } else if (current_volume >= 6 && current_volume < 10) run {
            increaseinSound = current_volume + increase4dec
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, increaseinSound, 0)
            current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

            //handle_volume_btn(current_volume);
        } else if (current_volume >= 10 && current_volume < 13) {
            increaseinSound = current_volume + increase3dec
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, increaseinSound, 0)
            current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        } else if (current_volume >= 13 && current_volume <= 15) {
            btnVolHigh?.setImageResource(R.drawable.volsuper)
            btnVolLow?.setImageResource(R.drawable.vol_minus_new)
            increaseinSound = current_volume + increase2dec
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, increaseinSound, 0)
            current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        } else if (current_volume <= 1) {
            btnVolLow?.setImageResource(R.drawable.vol_minus_new)
            increaseinSound = current_volume + increase2dec
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, increaseinSound, 0)
            current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        }
        //Toast.makeText(context, current_volume.toString(), Toast.LENGTH_SHORT).show()
    }


    fun playMusic() {
        if (!isListOpen) {
            imageDrop.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_down))
            //txtCurrentMusic.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0);
            //recyclerList.visibility = View.VISIBLE
            hidePlayer()
            val users = ArrayList<String>()
            users.add("Heart London ")
            users.add("BBC Radio 1 ")
            users.add("BBC Radio 2 ")
            users.add("BBC Radio 4 ")
            users.add("Capital FM ")

//            val obj_adapter = CustomRadioAdapter(users, object : BtnClickListener {
//                override fun onBtnClick(position: Int) {
//                    isListOpen = false
//
//                    for (i in 0..position) {
//                        if (i == position) {
//                            playAudio(jcAudios.get(position))
//                            //recyclerList.visibility = View.GONE
//                            imageDrop.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_right))
//                            showPlayer()
//                        }
//                    }
//                }
//            })
            //recyclerList.adapter = obj_adapter
            isListOpen = true
        } else {
            imageDrop.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_right))
            //txtCurrentMusic.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            showPlayer()
            //recyclerList.adapter = null
            isListOpen = false
        }
    }


    fun changeSpinnerInc() {
        if (spinner.selectedItemPosition == 0) {
            spinner.setSelection(1)
        } else if (spinner.selectedItemPosition == 1) {
            spinner.setSelection(2)
        } else if (spinner.selectedItemPosition == 2) {
            spinner.setSelection(3)
        } else if (spinner.selectedItemPosition == 3) {
            spinner.setSelection(4)
        } else if (spinner.selectedItemPosition == 4) {
            spinner.setSelection(0)
        }
    }


    fun changeSpinnerDec() {
        if (spinner.selectedItemPosition == 0) {
            spinner.setSelection(4)
        } else if (spinner.selectedItemPosition == 1) {
            spinner.setSelection(0)
        } else if (spinner.selectedItemPosition == 2) {
            spinner.setSelection(1)
        } else if (spinner.selectedItemPosition == 3) {
            spinner.setSelection(2)
        } else if (spinner.selectedItemPosition == 4) {
            spinner.setSelection(3)
        }
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {
        jcbuttonclicked?.buttonClicked()
        Log.d("sdsd", "sdsd")
        pause()
        showPlayer()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        spinner.setOnItemSelectedListener(this)
        Log.d("sdsd", "sdsd")
        jcbuttonclicked?.buttonClicked()
        for (i in 0..position) {
            if (i == position) {
//                 Handler().postDelayed({
                playState = 1
//        }, 100)
                playAudio(jcAudios.get(position))
                imageDrop.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_right))
                showPlayer()
            }
        }
    }

//    class CustomRadioAdapter(val userList: ArrayList<String>, val btnlistener: BtnClickListener) : RecyclerView.Adapter<CustomRadioAdapter.ViewHolder>() {
//        companion object {
//            var mClickListener: BtnClickListener? = null
//        }
//
//
//        //this method is returning the view for each item in the list
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomRadioAdapter.ViewHolder {
//            val v = LayoutInflater.from(parent.context).inflate(R.layout.radio_station_list, parent, false)
//            return ViewHolder(v)
//        }
//
//        //this method is binding the data on the list
//        override fun onBindViewHolder(holder: CustomRadioAdapter.ViewHolder, position: Int) {
//            mClickListener = btnlistener
//            holder.bindItems(userList[position], position)
//
//        }
//
//        //this method is giving the size of the list
//        override fun getItemCount(): Int {
//            return userList.size
//        }
//
//        //the class is hodling the list view
//        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//            var selectedPosition = -1
//            fun bindItems(user: String, position: Int) {
//                if (selectedPosition == position)
//                    itemView.listView.setBackgroundColor(Color.parseColor("#f0f0f0"));
//                else
//                    itemView.listView.setBackgroundColor(Color.parseColor("#ffffff"));
//
//                itemView.text.text = user
//                itemView.text.setOnClickListener(object : View.OnClickListener {
//                    override fun onClick(v: View?) {
//                        if (mClickListener != null)
//                            mClickListener?.onBtnClick(layoutPosition)
//                    }
//                })
//            }
//        }
//
//    }

    open class MyStationListAdapter(context: Context, resource: Int, list: Array<String>) :
            ArrayAdapter<String>(context, resource, list) {

        var resource: Int
        var list: Array<String>
        var vi: LayoutInflater

        init {
            this.resource = resource
            this.list = list
            this.vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }


        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            var holder: ViewHolder
            var retView: View

            if (convertView == null) {
                retView = vi.inflate(resource, null)
                holder = ViewHolder()
                holder.text = retView.findViewById(R.id.text) as TextView?
                //holder.text?.setTypeface(utils._montserrat_bold)
                retView.tag = holder

            } else {
                holder = convertView.tag as ViewHolder
                retView = convertView
            }

            return retView
        }

        internal class ViewHolder {
            var text: TextView? = null
        }
    }

    fun hideSpinnerDropDown(spinner: Spinner) {
        try {
            val method = Spinner::class.java.getDeclaredMethod("onDetachedFromWindow")
            method.setAccessible(true)
            method.invoke(spinner)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}