package com.mygdx.sonar_client;

import java.io.*;
import java.net.*;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Client extends ApplicationAdapter {
	private SpriteBatch batch;
	private ShapeRenderer sr;

	private InputHandler inputHandler;
	private InputMultiplexer inputMultiplexer;

	private Socket s;
	private OutputStream output;
	private PrintWriter writer;
	public static InputThread inputThread;

	private BitmapFont font;
	private GlyphLayout layout;

	private Stage stage;
	public TextField field;

	private Button chatUp;
	private Button chatDown;
	private Button chatSend;

	private String messages[];
	private int message = 0;

	private Role role;

	@Override
	public void create () {

		Assets.load();

		inputHandler = new InputHandler(this);
		inputMultiplexer = new InputMultiplexer();

		batch = new SpriteBatch();
		sr = new ShapeRenderer();
		font = new BitmapFont();
		layout = new GlyphLayout();
		stage = new Stage();

		Skin chatFieldSkin = new Skin(Gdx.files.internal("chat_field/chat_field.json"));

		field = new TextField("testing", chatFieldSkin);
		field.setPosition(5, 5);
		field.setSize(340, 20);

		chatDown = new Button(Assets.buttonSkin, "chat_down");
		chatUp = new Button(Assets.buttonSkin, "chat_up");
		chatSend = new Button(Assets.buttonSkin, "chat_send");
		chatDown.setSize(20, 20);
		chatUp.setSize(20, 20);
		chatSend.setSize(20, 20);
		chatDown.setPosition(Settings.CHAT_BOX_WIDTH - 25, 5);
		chatUp.setPosition(Settings.CHAT_BOX_WIDTH - 25, Settings.CHAT_BOX_HEIGHT - 25);
		chatSend.setPosition(350, 5);

		chatDown.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent e, float x, float y) {
				cycleMessagesDown();
			}
		});
		chatUp.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent e, float x, float y) {
				cycleMessagesUp();
			}
		});
		chatSend.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent e, float x, float y) {
				send();
			}
		});

		stage.addActor(chatDown);
		stage.addActor(chatUp);
		stage.addActor(chatSend);
		stage.addActor(field);

		messages = new String[Settings.CHAT_HISTORY_LENGTH];
		for(int i = 0; i < messages.length; i++) {
			messages[i] = "";
		}

		role = new RadioOperator(batch);

		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(role.getStage());
		Gdx.input.setInputProcessor(inputMultiplexer);

		try {
			s = new Socket(InetAddress.getByName(Settings.IP), Settings.PORT);
			output = s.getOutputStream();
			writer = new PrintWriter(output, true);
			inputThread = new InputThread(s, this);
			inputThread.start();

		} catch(UnknownHostException e) {
			System.out.println("Server not found: " + e.getMessage());
		} catch(IOException e) {
			System.out.println("I/O error: " + e.getMessage());
		}

	}

	/* Sends a string to the server from the TextField.
	 *
	 */
	public void send() {
		String message = field.getText();
		if(!message.equals("")) {
			writer.println(message);
			field.setText("");
		}
		if(message.equals("exit")) {
			inputThread.exit();
		}
	}

	/* Adds a chat message and cycles all other chat messages down.
	 * More recent messages have a lower index.
	 */
	public void addMessage(String message) {
		for(int i = messages.length-1; i > 0; i--) {
			messages[i] = messages[i-1];
		}
		messages[0] = message;
	}

	/* Cycles the messages displayed up.
	 * (Toward older messages)
	 */
	public void cycleMessagesUp() {
		if(message < Settings.CHAT_HISTORY_LENGTH - Settings.CHAT_HEIGHT) {
			message++;
		}
	}

	/* Cycles the messages displayed down.
	 * (Toward newer messages)
	 */
	public void cycleMessagesDown() {
		if(message > 0) {
			message--;
		}
	}

	@Override
	public void render () {

		inputHandler.update();

		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		sr.setAutoShapeType(true);
		sr.begin(ShapeRenderer.ShapeType.Filled);
		sr.setColor(30f/255,30f/255,30f/255,1);
		sr.rect(0,0, Settings.CHAT_BOX_WIDTH, Settings.CHAT_BOX_HEIGHT);
		sr.setColor(120f/255, 120f/255, 120f/255, 1);
		sr.rect(Settings.CHAT_BOX_WIDTH - 23, 30, 16, Settings.CHAT_BOX_HEIGHT - 60);
		sr.setColor(80f/255, 80f/255, 80f/255, 0.8f);
		sr.rect(Settings.CHAT_BOX_WIDTH - 23, 30 + (Settings.CHAT_BOX_HEIGHT-76) * ((float)message / (Settings.CHAT_HISTORY_LENGTH-Settings.CHAT_HEIGHT)), 16, 16);
		sr.end();

		batch.begin();
		batch.enableBlending();

		font.getData().setScale(1f);
		layout.setText(font, "texting is epic and making a good gui is for losers");
		font.draw(batch, layout, 700, 50);

		font.setColor(Color.WHITE);
		for(int i = 0; i < Settings.CHAT_HEIGHT; i++) {
			layout.setText(font, messages[message + i]);
			font.draw(batch, layout, 10, 45+20*i);
		}

		batch.end();

		if(!(role == null)) {
			role.update();
		}

		batch.begin();
		batch.enableBlending();
		stage.act();
		stage.draw();
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		if(!(role == null)) {
			role.getStage().getViewport().update(width, height, true);
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		sr.dispose();
	}

}
