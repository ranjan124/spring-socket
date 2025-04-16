package com.shieldteq.socket;

import com.shieldteq.socket.service.LocalSocketAcceptor;
import io.rsocket.core.RSocketServer;
import io.rsocket.transport.netty.server.CloseableChannel;
import io.rsocket.transport.netty.server.TcpServerTransport;

public class Server {
    public static void main(String[] args) {
        RSocketServer server = RSocketServer.create(new LocalSocketAcceptor());
        CloseableChannel closeableChannel = server.bindNow(TcpServerTransport.create(8888));
        closeableChannel.onClose().block();
    }
}
