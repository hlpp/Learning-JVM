package constant;

/**
 * 
 * @author hulang
 */
public class ConstantPool {
    /** u2 ���ڳ�Ա��+1 */
    public int constantPoolCount;
    
    /** cp_index = u2, �ڳ�������,������1��constantPoolCount-1,Ӧӳ��Ϊ0~constantPoolCount-2 */
    public ConstantInfo[] array;
}
