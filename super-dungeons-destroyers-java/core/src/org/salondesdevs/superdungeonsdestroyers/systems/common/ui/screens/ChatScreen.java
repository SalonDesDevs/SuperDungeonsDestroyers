package org.salondesdevs.superdungeonsdestroyers.systems.common.ui.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTextField;
import org.salondesdevs.superdungeonsdestroyers.library.chat.ChatMessage;
import org.salondesdevs.superdungeonsdestroyers.systems.ingame.ClientChat;

import javax.inject.Inject;

public class ChatScreen extends Screen {

    @Inject
    ClientChat clientChat;

    @Inject
    public void addActors() {
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        VerticalGroup verticalGroup = new VerticalGroup().space(80f);
        table.add(verticalGroup);
        stage.addActor(table);

        Table messages = new Table();
        messages.center().setFillParent(true);

        for (ChatMessage message : clientChat.messages) {
            System.out.println("message :" + message.content);
            messages.add(new VisLabel(message.content));
        }

        VisScrollPane scrollPane = new VisScrollPane(messages);
        verticalGroup.addActor(scrollPane);

        VisTextField chatField = new VisTextField();
        verticalGroup.addActor(chatField);

    }
}
