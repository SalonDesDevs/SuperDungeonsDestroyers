package org.salondesdevs.superdungeonsdestroyers.systems.common.ui.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import org.salondesdevs.superdungeonsdestroyers.library.events.EventHandler;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import org.salondesdevs.superdungeonsdestroyers.events.ChatEvent;
import org.salondesdevs.superdungeonsdestroyers.library.chat.ChatChannel;
import org.salondesdevs.superdungeonsdestroyers.library.chat.ChatMessage;
import org.salondesdevs.superdungeonsdestroyers.library.events.core.EventBus;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.FromClientChat;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.ClientChat;
import org.salondesdevs.superdungeonsdestroyers.utils.BackgroundColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ChatScreen extends Screen {

    private static final Logger logger = LoggerFactory.getLogger(ChatScreen.class);

    @Inject
    ClientChat clientChat;

    @Inject
    NetworkSystem networkSystem;

    VisTextField chatField;
    VerticalGroup messages;
    HorizontalGroup fieldAndSendButton;

    @Inject
    EventBus eventBus;

    @Inject
    public void initialize() {
        eventBus.register(this);
    }


    @Override
    public void onClosed() {
        // Override super, te prevent EventBus::unregister
        fieldAndSendButton.setVisible(false);
    }

    @Override
    public void onDisplayed() {
        // Override super, te prevent EventBus::register
        fieldAndSendButton.setVisible(true);
    }

    @Inject
    public void addActors() {
        Table table = new Table();
        table.setBounds(10, 220, 180, 200);
//        table.center();
//        table.setFillParent(true);

        {
            VerticalGroup globalLayout = new VerticalGroup().space(15.0f);
            {

                Table messagesAndSendBackground = new Table();
                table.setBackground(new BackgroundColor(new Color(0x111111dd)));
                {
                    VerticalGroup messagesAndButtons = new VerticalGroup().space(5.0f);
                    {
                        messages = new VerticalGroup().space(2.0f);
                        messages.setHeight(100.0f);
                        messages.left();

                        for (ChatMessage message : clientChat.messages) {
                            appendMessage(message);
                        }

                        VisScrollPane scrollPane = new VisScrollPane(messages);
                        messagesAndButtons.addActor(scrollPane);

                        fieldAndSendButton = new HorizontalGroup().space(5.0f);

                        chatField = new VisTextField();
                        chatField.setMessageText("Type here...");
                        chatField.addListener(new InputListener() {
                            @Override
                            public boolean keyUp(InputEvent event, int keycode) {
                                if (keycode == Input.Keys.ENTER) {
                                    sendText();
                                    return true;
                                }
                                return false;
                            }
                        });
                        fieldAndSendButton.addActor(chatField);

                        VisTextButton sendButton = new VisTextButton("Send");
                        sendButton.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                sendText();
                            }
                        });
                        fieldAndSendButton.addActor(sendButton);
                        messagesAndButtons.addActor(fieldAndSendButton);
                    }
                    messagesAndButtons.setFillParent(true);
                    messagesAndSendBackground.add(messagesAndButtons);
                }

                globalLayout.addActor(messagesAndSendBackground);
            }
            table.add(globalLayout);
        }
        stage.addActor(table);
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        this.appendMessage(event.getMessage());
    }

    private void appendMessage(ChatMessage message) {
        VisLabel label = new VisLabel(message.toDisplayString());
        messages.addActor(label);
    }

    private void sendText() {
        if (!chatField.getText().isEmpty()) {
            networkSystem.send(new FromClientChat(ChatMessage.text(chatField.getText(), ChatChannel.GAME), ChatChannel.GAME));
            chatField.clearText();
        }
    }
}
