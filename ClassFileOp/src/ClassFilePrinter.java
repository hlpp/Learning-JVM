import static java.lang.System.out;

import constant.*;

/**
 * 
 * @author hulang
 */
public class ClassFilePrinter {
    private ClassFile classFile;
    private ConstantPool cp;
    
    public void print(ClassFile classFile) {
        this.classFile = classFile;
        this.cp = classFile.cp;
        
        out.printf("ħ��: 0x%X\n", classFile.magic);
        out.printf("�汾��: %d.%d\n", classFile.majorVersion, classFile.minorVersion);
        
        printConstantPool();
        
        printAccessFlag(classFile.accessFlags);
        
        out.print("����: ");
        printClassConstant(classFile.thisClass);
        
        out.print("����: ");
        if (classFile.superClass != 0) {
            printClassConstant(classFile.superClass);
        } else {
            out.println("��");
        }
        
        printInterfaces();
        
        printFields();
        
        printMethods();
        
        printAttributes(classFile.attributesCount, classFile.attributes, "");
    }

    private void printMethods() {
        out.println("------------");
        out.printf("��������: %d\n", classFile.methodsCount);

        if (classFile.methodsCount > 0) {
            out.println("��������:");
            for (MethodInfo methodInfo : classFile.methods) {
                out.print("    ");
                printAccessFlag(methodInfo.accessFlags);
                out.print("    ����: ");
                printUTF8Constant(methodInfo.nameIndex);
                out.print("    ������: ");
                printUTF8Constant(methodInfo.descriptorIndex);
                out.println();
                printAttributes(methodInfo.attributesCount, methodInfo.attributes, "    ");
                out.println();
            }
        }
    }

    private void printFields() {
        out.println("------------");
        out.printf("�ֶμ���: %d\n", classFile.fieldsCount);

        if (classFile.fieldsCount > 0) {
            out.println("�ֶ�����:");
            for (FieldInfo fieldInfo : classFile.fields) {
                out.print("    ");
                printAccessFlag(fieldInfo.accessFlags);
                out.print("    ����: ");
                printUTF8Constant(fieldInfo.nameIndex);
                out.print("    ������: ");
                printUTF8Constant(fieldInfo.descriptorIndex);
                out.println();
                printAttributes(fieldInfo.attributesCount, fieldInfo.attributes, "    ");
                out.println();
            }
        }
    }
    
    private void printAttributes(int attributesCount, AttributeInfo[] attributes, String indent) {
        out.print(indent);
        out.printf("���Լ���: %d\n", attributesCount);

        if (attributesCount > 0) {
            out.print(indent);
            out.println("��������:");
            for (AttributeInfo attrInfo : attributes) {
                out.print(indent);
                out.print("    ����: ");
                printUTF8Constant(attrInfo.attributeNameIndex);
                out.print(indent);
                out.printf("    ������Ϣ����: %d", attrInfo.attributeLength);
                out.print(indent);
                out.print("    ������Ϣ����: ");
                for (int i = 0, ilen = attrInfo.info.length; i < ilen; i++) {
                    out.printf("0x%X ", attrInfo.info[i]);
                }
                out.println();
            }
        }
    }

    private void printInterfaces() {
        out.println("------------");
        out.printf("�ӿڼ���: %d\n", classFile.interfacesCount);

        if (classFile.interfacesCount > 0) {
            out.println("�ӿ�����:");
            for (int interfacei : classFile.interfaces) {
                printClassConstant(interfacei);
            }
        }
    }

    private void printAccessFlag(int accessFlags) {
        out.printf("���ʱ��: 0x%X  ", accessFlags);

        switch (accessFlags) {
        case AccessFlag.PUBLIC:
            out.print("public");
            break;
        case AccessFlag.PRIVATE:
            out.print("private");
            break;
        case AccessFlag.PROTECTED:
            out.print("protected");
            break;
        case AccessFlag.STATIC:
            out.print("static");
            break;
        case AccessFlag.FINAL:
            out.print("final");
            break;
        case AccessFlag.SUPER:
            out.print("super");
            break;
        case AccessFlag.VOLATILE:
            out.print("volatile");
            break;
        case AccessFlag.TRANSIENT:
            out.print("transient");
            break;
        case AccessFlag.INTERFACE:
            out.print("interface");
            break;
        case AccessFlag.ABSTRACT:
            out.print("abstract");
            break;
        case AccessFlag.SYNTHETIC:
            out.print("synthetic");
            break;
        case AccessFlag.ANNOTATION:
            out.print("annotation");
            break;
        case AccessFlag.ENUM:
            out.print("enum");
            break;
        }
        out.println();
    }

    private void printConstantPool() {
        ConstantPool cp = classFile.cp;
        out.println("------------");
        out.printf("�����ؼ���: %d\n", cp.constantPoolCount);
        out.println("������:");
        ConstantInfo constInfo = null;
        for (int itemIndex = 0, cplen = cp.constantPoolCount - 1; itemIndex < cplen; itemIndex++) {
            constInfo = cp.array[itemIndex];
            out.printf("��%d��:\n", itemIndex + 1);
            switch (constInfo.tag) {
            case ConstantPoolTag.CLASS:
                printClassConstant((ClassConstantInfo)constInfo);
                break;
            case ConstantPoolTag.FIELD_REF:
                out.println("�ֶ�����");
                out.print("    ������: ");
                printClassConstant(((FieldRefConstantInfo)constInfo).classIndex);
                out.print("    ");
                printNameAndTypeConstant(((FieldRefConstantInfo)constInfo).nameAndTypeIndex);
                break;
            case ConstantPoolTag.METHOD_REF:
                out.println("��������");
                out.print("    ������: ");
                printClassConstant(((MethodRefConstantInfo)constInfo).classIndex);
                out.print("    ");
                printNameAndTypeConstant(((MethodRefConstantInfo)constInfo).nameAndTypeIndex);
                out.println();
                break;
            case ConstantPoolTag.INTERFACE_METHOD_REF:
                out.println("�ӿڷ�������");
                out.print("    ������: ");
                printClassConstant(((InterfaceMethodRefConstantInfo)constInfo).classIndex);
                out.print("    ");
                printNameAndTypeConstant(((InterfaceMethodRefConstantInfo)constInfo).nameAndTypeIndex);
                out.println();
                break;
            case ConstantPoolTag.STRING:
                out.print("�ַ���: ");
                printUTF8Constant(((StringConstantInfo)constInfo).stringIndex);
                out.println();
                break;
            case ConstantPoolTag.INTEGER:
                break;
            case ConstantPoolTag.FLOAT:
                break;
            case ConstantPoolTag.LONG:
                break;
            case ConstantPoolTag.DOUBLE:
                break;
            case ConstantPoolTag.NAME_AND_TYPE:
                printNameAndTypeConstant((NameAndTypeConstantInfo)constInfo);
                out.println();
                break;
            case ConstantPoolTag.UTF8:
                printUTF8Constant((UTF8ConstantInfo)constInfo);
                out.println();
                break;
            case ConstantPoolTag.METHOD_HANDLE:
                out.println("�������");
                //TODO: referenceKind
                //cp.array[ ((MethodHandleConstantInfo)constInfo).referenceIndex]
                out.println();
                break;
            case ConstantPoolTag.METHOD_TYPE:
                out.println("��������");
                out.print("    ");
                printUTF8Constant(((NameAndTypeConstantInfo)constInfo).descriptorIndex);
                out.println();
                break;
            case ConstantPoolTag.INVOKE_DYNAMIC:
                out.println("Invoke Dynamic");
                out.print("    ");
                //TODO: methods
                printUTF8Constant(((InvokeDynamicConstantInfo)constInfo).nameAndTypeIndex);
                out.println();
                break;
            }
        }
        out.println("------------");
    }

    private void printNameAndTypeConstant(NameAndTypeConstantInfo constInfo) {
        out.print("���ƺ�����:");
        out.print("   ����=");
        printUTF8Constant((UTF8ConstantInfo)cp.array[ ((NameAndTypeConstantInfo)constInfo).nameIndex - 1]);
        out.print("   ������=");
        printUTF8Constant((UTF8ConstantInfo)cp.array[ ((NameAndTypeConstantInfo)constInfo).descriptorIndex - 1]);
    }
    
    /**
     * @param index cp_index
     */
    private void printNameAndTypeConstant(int index) {
        printNameAndTypeConstant((NameAndTypeConstantInfo)cp.array[index - 1]);
    }
    
    private void printClassConstant(ClassConstantInfo cci) {
        out.print("��: ����: ");
        printUTF8Constant((UTF8ConstantInfo)cp.array[((ClassConstantInfo)cci).nameIndex - 1]);
        out.println();
    }
    
    /**
     * @param index cp_index
     */
    private void printClassConstant(int index) {
        printClassConstant((ClassConstantInfo)cp.array[index - 1]);
    }
    
    private void printUTF8Constant(UTF8ConstantInfo constInfo) {
        out.print("UTF8: ");
        // ֻ֧��ascii
        byte[] asciiBytes = new byte[constInfo.bytes.length];
        for (int i = 0; i < asciiBytes.length; i++)
            asciiBytes[i] = (byte)constInfo.bytes[i];
        out.printf("%s", new String(asciiBytes));
    }
    
    /**
     * @param index cp_index
     */
    private void printUTF8Constant(int index) {
        printUTF8Constant((UTF8ConstantInfo)cp.array[index - 1]);
    }
}
