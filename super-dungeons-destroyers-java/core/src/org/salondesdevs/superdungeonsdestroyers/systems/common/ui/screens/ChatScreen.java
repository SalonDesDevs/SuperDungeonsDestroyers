package org.salondesdevs.superdungeonsdestroyers.systems.common.ui.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.google.common.eventbus.Subscribe;
import com.kotcrab.vis.ui.widget.*;
import org.salondesdevs.superdungeonsdestroyers.events.ChatEvent;
import org.salondesdevs.superdungeonsdestroyers.library.chat.ChatChannel;
import org.salondesdevs.superdungeonsdestroyers.library.chat.ChatMessage;
import org.salondesdevs.superdungeonsdestroyers.library.packets.fromclient.FromClientChat;
import org.salondesdevs.superdungeonsdestroyers.systems.common.network.NetworkSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.common.ui.UiSystem;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.ClientChat;
import org.salondesdevs.superdungeonsdestroyers.utils.BackgroundColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class ChatScreen extends Screen {

    private static final Logger logger = LoggerFactory.getLogger(ChatScreen.class);


    @Inject
    ClientChat clientChat;

    @Inject
    UiSystem uiSystem;

    @Inject
    NetworkSystem networkSystem;

    VisTextField chatField;
    VerticalGroup messages;

    @Inject
    public void addActors() {
        Table table = new Table();
        table.setBackground(new BackgroundColor(new Color(0.4f, 0.4f, 0.4f, 0.4f)));
        table.center();
        table.setFillParent(true);

        {
            VerticalGroup globalLayout = new VerticalGroup().space(15.0f);
            {

                Table messagesAndSendBackground = new Table();
                table.setBackground(new BackgroundColor(new Color(0x111111dd)));
                {
                    VerticalGroup messagesAndButtons = new VerticalGroup().space(5.0f);
                    {
                        messages = new VerticalGroup().space(2.0f);
                        messages.left();
                        messages.setHeight(200.0f);

                        for (ChatMessage message : clientChat.messages) {
                            appendMessage(message);
                        }

                        VisScrollPane scrollPane = new VisScrollPane(messages);
                        messagesAndButtons.addActor(scrollPane);

                        HorizontalGroup horizontalGroup = new HorizontalGroup().space(5.0f);

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
                        horizontalGroup.addActor(chatField);

                        VisTextButton sendButton = new VisTextButton("Send");
                        sendButton.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                sendText();
                            }
                        });
                        horizontalGroup.addActor(sendButton);
                        messagesAndButtons.addActor(horizontalGroup);
                    }
                    messagesAndSendBackground.add(messagesAndButtons);
                }

                globalLayout.addActor(messagesAndSendBackground);

                VisTextButton closeButton = new VisTextButton("Close chat");
                closeButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        uiSystem.closeScreen();
                    }
                });
                globalLayout.addActor(closeButton);
            }
            table.add(globalLayout);
        }
        stage.addActor(table);
    }

    @Subscribe
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
