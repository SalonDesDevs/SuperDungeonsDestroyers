package org.salondesdevs.superdungeonsdestroyers.systems.common.network;

import SDD.Response.Pong;
import SDD.Response.Response;
import SDD.Response.Result;
import SDD.Response.Task;
import net.wytrem.ecs.*;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class NetworkHandler extends BaseSystem {

    List<Response> responsesToHandle = new ArrayList<>();


    @Override
    public void process() {
        synchronized (responsesToHandle) {
            responsesToHandle.forEach(this::handleResponse);
        }
    }

    private void handleResponse(Response response) {
        for (int i = 0; i < response.tasksLength(); i++) {
            Task task = response.tasks(i);

            handleTask(task);
        }
    }

    private void handleTask(Task task) {
        switch (task.resultType()) {
            case Result
                    .Pong:
                Pong pong = (Pong) task.result(new Pong());
                System.out.println("Received Pong with value " + pong.value());
            break;
            default:
                System.out.println("Handling received task with resultType" + task.resultType());
        }
    }
}
