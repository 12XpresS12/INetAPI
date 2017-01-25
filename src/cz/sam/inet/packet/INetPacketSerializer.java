package cz.sam.inet.packet;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;

import cz.sam.inet.INetProtocol;

public class INetPacketSerializer {
	
	private DataOutputStream out;
	private DataInputStream in;
	
	public INetPacketSerializer(DataOutputStream out) {
		this(out, null);
	}
	
	public INetPacketSerializer(DataInputStream in) {
		this(null, in);
	}
	
	public INetPacketSerializer(DataOutputStream out, DataInputStream in) {
		this.out = out;
		this.in = in;
	}
	
	public void write(int par1) throws Exception {
		this.out.write(par1);
	}
	
	public void writeLong(long l) throws Exception {
		this.out.writeLong(l);
	}
	
	public long readLong() throws Exception {
		return this.in.readShort();
	}
	
	public void writeInt(int par1) throws Exception {
		this.out.writeInt(par1);
	}
	
	public int read() throws Exception {
		return this.in.read();
	}
	
	public int readInt() throws Exception {
		return this.in.readInt();
	}
	
	public float readFloat() throws Exception {
		return this.in.readFloat();
	}
	
	public void writeFloat(float f) throws Exception {
		this.out.writeFloat(f);
	}
	
	public void writeByteArray(byte[] par1) throws Exception {
		this.writeInt(par1.length);
		this.out.write(par1);
	}
	
	public byte[] readByteArray() throws Exception {
		byte[] b = new byte[this.readInt()];
		this.in.read(b);
		return b;
	}
	
	public void writeChars(String par1) throws Exception {
		this.out.writeChars(par1);
	}
	
	public char readChar() throws Exception {
		return this.in.readChar();
	}
	
	public void writeImage(BufferedImage par1, String imageType) throws Exception {
		ImageIO.write(par1, imageType, this.out);
	}
	
	public BufferedImage readImage() throws Exception {
		return ImageIO.read(this.in);
	}
	
	public void writeString(String par1) throws Exception {
		this.writeInt(par1.length());
		this.writeChars(par1);
	}
	
	public String readString() throws Exception {
		int length = this.readInt();
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < length; i++) {
			builder.append(this.readChar());
		}
		return builder.toString();
	}
	
	public void writeBoolean(boolean par1) throws Exception {
		this.out.writeBoolean(par1);
	}
	
	public boolean readBoolean() throws Exception {
		return this.in.readBoolean();
	}
	
	public void writeProtocol(INetProtocol protocol) throws Exception {
		this.out.writeInt(protocol.getID());
	}
	
	public INetProtocol readProtocol() throws Exception {
		return INetProtocol.fromId(this.readInt());
	}
	
	public static INetPacketSerializer fromSocket(Socket socket) throws Exception {
		return new INetPacketSerializer(new DataOutputStream(socket.getOutputStream()), new DataInputStream(socket.getInputStream()));
	}
	
}
