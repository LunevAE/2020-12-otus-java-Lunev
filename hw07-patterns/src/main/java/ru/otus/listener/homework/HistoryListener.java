package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.ArrayList;
import java.util.List;

//todo: 4. Сделать Listener для ведения истории: старое сообщение - новое (подумайте, как сделать, чтобы сообщения не портились)
public class HistoryListener implements Listener {

    private List<History> historyList = new ArrayList<>();

    public List<History> getHistoryList() {
        return historyList;
    }

    @Override
    public void onUpdated(Message oldMessage, Message newMessage) {
        historyList.add(new History(oldMessage.deepCopy(), newMessage.deepCopy()));
    }


    private static class History {
        Message oldMessage;
        Message newMessage;

        public History(Message oldMessage,
                       Message newMessage) {
            this.oldMessage = oldMessage;
            this.newMessage = newMessage;
        }


        @Override
        public String toString() {
            return "History{\r\n" +
                    "oldMessage=" + oldMessage + ",\r\n" +
                    "newMessage=" + newMessage +
                    '}';
        }
    }
}