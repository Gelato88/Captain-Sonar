package com.mygdx.sonar_client;

import java.io.*;
import java.net.*;
import java.util.*;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Client extends ApplicationAdapter {
	private SpriteBatch batch;
	private ShapeRenderer sr;

	Texture img;

	private static int PORT = 24644;

	private Socket s;
	private OutputStream output;
	private PrintWriter writer;
	private Scanner scanner;
	public static InputThread inputHandler;
	//public static OutputThread outputHandler;

	private BitmapFont font;
	private GlyphLayout layout;

	public static String lastMessage;

	private Stage stage;
	private TextField field;

	private Button chatUp;
	private Button chatDown;
	private Button chatSend;

	private String messages[];
	private int message = 0;

	@Override
	public void create () {




		lastMessage = "Enter name.";

		batch = new SpriteBatch();
		sr = new ShapeRenderer();

		font = new BitmapFont();
		layout = new GlyphLayout();

		lastMessage = "";

		stage = new Stage();

		Skin chatDownSkin = new Skin(Gdx.files.internal("chat_down/chat_down.json"));
		Skin chatUpSkin = new Skin(Gdx.files.internal("chat_up/chat_up.json"));
		Skin chatSendSkin = new Skin(Gdx.files.internal("chat_send/chat_send.json"));
		Skin chatFieldSkin = new Skin(Gdx.files.internal("chat_field/chat_field.json"));

		field = new TextField("testing", chatFieldSkin);
		field.setPosition(5, 5);
		field.setSize(340, 20);

		chatDown = new Button(chatDownSkin);
		chatUp = new Button(chatUpSkin);
		chatSend = new Button(chatSendSkin);
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
		Gdx.input.setInputProcessor(stage);

		messages = new String[Settings.CHAT_HISTORY_LENGTH];
		for(int i = 0; i < messages.length; i++) {
			messages[i] = "";
		}


		try {
			s = new Socket(InetAddress.getByName("192.168.2.115"), PORT);
			output = s.getOutputStream();
			writer = new PrintWriter(output, true);

			inputHandler = new InputThread(s, this);
			//outputHandler = new OutputThread(s);
			inputHandler.start();
			//outputHandler.start();


			scanner = new Scanner(System.in);
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
		System.out.println("SEND");
		String message = field.getText();
		writer.println(message);
		field.setText("");
		button.setChecked(false);
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

		batch.begin();
		batch.enableBlending();

		stage.act();
		stage.draw();


		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

}
