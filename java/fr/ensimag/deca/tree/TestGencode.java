/* A manual test for the initial sketch of code generation included in
 * students skeleton.
 *
 * It is not intended to still work when code generation has been updated.
 */
package fr.ensimag.deca.tree;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import fr.ensimag.deca.CompilationType;
import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.context.VoidType;
import fr.ensimag.deca.tools.SymbolTable;

/**
 *
 * @author Ensimag
 * @date 01/01/2019
 */
public class TestGencode {


	private static Identifier typeInt, typeFloat, typeBool, typeVoid, typeA;
	
	private static DeclVar declInt(DecacCompiler compiler, Identifier var, int value) {
        var.setDefinition(new VariableDefinition(new IntType(compiler.getTable().create("int")), new Location(0, 0, null)));
        var.setType(new IntType(compiler.getTable().create("int")));
        
        Initialization init = new Initialization(new IntLiteral(value));
        
        DeclVar decVar = new DeclVar(typeInt, var, init);
        return decVar;
	}
	
	private static DeclVar declFloat(DecacCompiler compiler, Identifier var, float value) {
        var.setDefinition(new VariableDefinition(new FloatType(compiler.getTable().create("float")), new Location(0, 0, null)));
        var.setType(new FloatType(compiler.getTable().create("float")));
        
        Initialization init = new Initialization(new FloatLiteral(value));
        DeclVar decVar = new DeclVar(typeFloat, var, init);
        return decVar;
	}
	
	private static DeclVar declBool(DecacCompiler compiler, Identifier var, boolean value) {
        var.setDefinition(new VariableDefinition(new BooleanType(compiler.getTable().create("boolean")), new Location(0, 0, null)));
        var.setType(new BooleanType(compiler.getTable().create("boolean")));
        
        Initialization init = new Initialization(new Equals(new IntLiteral(2), new IntLiteral(3)));
        DeclVar decVar = new DeclVar(typeBool, var, init);
        return decVar;
	}
	
	private static DeclVar declA(DecacCompiler compiler, Identifier var) {
        var.setDefinition(new VariableDefinition(new ClassType(compiler.getTable().create("A"), new Location(0, 0, null), null), new Location(0, 0, null)));
        var.setType(new ClassType(compiler.getTable().create("A"), new Location(0, 0, null), null));
        
        Identifier ident = new Identifier(compiler.getTable().create("A"));
        ident.setDefinition(new VariableDefinition(new ClassType(compiler.getTable().create("A"), new Location(0, 0, null), null), new Location(0, 0, null)));
        New newInit = new New(ident);
        newInit.setType(new ClassType(compiler.getTable().create("A"), new Location(0, 0, null), null));
        Initialization init = new Initialization(newInit);
        DeclVar decVar = new DeclVar(typeA, var, init);
        return decVar;
	}
	
	private static DeclField declFieldInt(DecacCompiler compiler, Identifier var, int value, Visibility v) {
        var.setDefinition(new VariableDefinition(new IntType(compiler.getTable().create("int")), new Location(0, 0, null)));
        var.setType(new IntType(compiler.getTable().create("int")));
        
        Initialization init = new Initialization(new IntLiteral(value));
        
        DeclField declField = new DeclField(v, typeInt, var, init);
        return declField;
	}
	
    public static void launchTest() {
    	CompilerOptions options = new CompilerOptions();
    	options.setCompilationType(CompilationType.IMA_68000);
    	DecacCompiler compiler = new DecacCompiler(options,null);
    	
    	ListDeclClass declAllClass = new ListDeclClass();
    	ListDeclVar declVariables = new ListDeclVar();
        ListInst instructions = new ListInst();
        SymbolTable symbolTable = new SymbolTable();

        AbstractProgram source = new Program(declAllClass, new Main(declVariables, instructions));
        
        typeBool = new Identifier(compiler.getTable().create("boolean"));
        typeBool.setDefinition(new TypeDefinition(new BooleanType(compiler.getTable().create("boolean")), Location.BUILTIN));
        typeInt = new Identifier(compiler.getTable().create("int"));
        typeInt.setDefinition(new TypeDefinition(new IntType(compiler.getTable().create("int")), Location.BUILTIN));
        typeFloat = new Identifier(compiler.getTable().create("float"));
        typeFloat.setDefinition(new TypeDefinition(new IntType(compiler.getTable().create("float")), Location.BUILTIN));
        typeVoid = new Identifier(compiler.getTable().create("void"));
        typeVoid.setDefinition(new TypeDefinition(new VoidType(compiler.getTable().create("void")), Location.BUILTIN));
        typeA = new Identifier(compiler.getTable().create("A"));
        typeA.setDefinition(new TypeDefinition(new ClassType(compiler.getTable().create("A"), new Location(0, 0, null), null), new Location(0, 0, null)));

        
        ListDeclField varsA = new ListDeclField();
        
        Identifier identx = new Identifier(compiler.getTable().create("x"));
        varsA.add(declFieldInt(compiler, identx, 3, Visibility.PUBLIC));
        Identifier obj = new Identifier(compiler.getTable().create("obj"));
        Selection x = new Selection(obj, identx);
        x.setType(new IntType(compiler.getTable().create("int")));
        ListDeclMethod declMethods = new ListDeclMethod();
        
        Identifier identClass = new Identifier(compiler.getSymbolTable().create("Object"));
        ClassDefinition objectDefinition = new ClassDefinition(new ClassType(compiler.getTable().create("Object"), Location.BUILTIN, null), new Location(0, 0, null), null); 
        identClass.setDefinition(objectDefinition);
        Identifier identA = new Identifier(compiler.getSymbolTable().create("A"));
        identA.setDefinition(new ClassDefinition(new ClassType(compiler.getTable().create("A"), new Location(0, 0, null), objectDefinition), new Location(0, 0, null), objectDefinition));
        DeclClass classA = new DeclClass(identA, identClass, varsA, declMethods);
        
        declAllClass.add(classA);
        
        Identifier methodIdent = new Identifier(compiler.getTable().create("coucou"));
        methodIdent.setDefinition(new MethodDefinition(new VoidType(compiler.getTable().create("void")), new Location(0, 0, null), null, 0));
        ListInst listInstrMethod = new ListInst();
        ListExpr listPrintMethod = new ListExpr();
        listPrintMethod.add(new StringLiteral("coucou from class A"));
        listInstrMethod.add(new Println(false, listPrintMethod));
        MethodBody methodBody = new MethodBody(new ListDeclVar(), listInstrMethod);
        DeclMethod method = new DeclMethod(typeVoid, methodIdent, new ListDeclParam(), methodBody);
        declMethods.add(method);
        
        ListExpr listPrint = new ListExpr();
        
        
        
        /*Identifier a = new Identifier(compiler.getTable().create("a"));
        declVariables.add(declInt(compiler, a, 3));//*/
        /*Identifier b = new Identifier(compiler.getTable().create("b"));
        declVariables.add(declFloat(compiler, b, 2f));//*/
        //Identifier c = new Identifier(compiler.getTable().create("c"));
        //declVariables.add(declBool(compiler, c, true));
        
        declVariables.add(declA(compiler, obj));
        
        listPrint.add(new StringLiteral("MAIN"));
        listPrint.add(new Plus(new IntLiteral(3), x));
        
        
        instructions.add(new Println(false, listPrint));
        //instructions.add(new MethodCall(obj, methodIdent, new ListExpr()));
        
    	
        System.out.println("---- From the following Abstract Syntax Tree ----");
        source.prettyPrint(System.out);
        System.out.println("---- We generate the following assembly code ----");
        source.codeGenProgram(compiler);        
        String result = compiler.displayIMAProgram();
        System.out.println(result);
		try {
			BufferedWriter buff = new BufferedWriter(new FileWriter("output.ass"));
			buff.write(result);
			buff.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }



    public static void main(String args[]) {
        launchTest();
    }
}
