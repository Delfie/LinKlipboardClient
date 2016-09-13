package server_manager;

public class LinKlipboard {

	public static final int MAX_GROUP = 10; // �ִ�� ������ �� �ִ� �׷� ��
	public static final int MAX_CLIENT = 10; // �� �׷쿡 �ִ�� ������ �� �ִ� Ŭ���̾�Ʈ ��

	public final static int NULL = -1;
	public final static int ACCESS_PERMIT = 100; // ���� ����
	public final static int ERROR_DUPLICATED_GROUPNAME = 200; // �ߺ��� �׷� �̸�
	public final static int ERROR_NO_MATCHED_GROUPNAME = 201; // �ش� �̸��� �׷� ����
	public final static int ERROR_PASSWORD_INCORRECT = 202; // �н����� ����ġ
	public final static int ERROR_SOCKET_CONNECTION = 203; // ���� ���� ����
	public final static int ERROR_DATA_TRANSFER = 204; // ������ �ۼ��� ����
	public final static int ERROR_TRYCATCH = 205;

	public final static int STRING_TYPE = 10;
	public final static int IMAGE_TYPE = 11;
	public final static int FILE_TYPE = 12;
}
