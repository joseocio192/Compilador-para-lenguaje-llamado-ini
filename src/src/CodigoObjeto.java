package src;

public class CodigoObjeto {
    private String codigoObjeto;
    private String codigoEnsamblador;

    public CodigoObjeto(String codigoEnsamblador) {
        this.codigoObjeto = "";
        this.codigoEnsamblador = codigoEnsamblador;
    }

    public void generarCodigoObjeto() {
        codigoObjeto = traducirDataAHex();
        codigoObjeto += traducirCodigoAHex();
    }

    public String traducirDataAHex() {
        StringBuilder hex = new StringBuilder();
        String[] lines = codigoEnsamblador.split("\n");
        boolean dataSection = false;

        for (String line : lines) {
            line = line.trim();
            if (line.equalsIgnoreCase(".DATA")) {
                dataSection = true;
                continue;
            }
            if (dataSection) {
                if (line.isEmpty() || line.startsWith(".")) {
                    break;
                }
                String[] parts = line.split("\\s+");
                if (parts.length >= 3) {
                    String variable = parts[0];
                    String type = parts[1];
                    String value = parts[2];

                    switch (type.toLowerCase()) {
                        case "db":
                            for (char c : value.toCharArray()) {
                                hex.append(String.format("%02X", (int) c));
                            }
                            break;
                        case "dd":
                            int intValue = Integer.parseInt(value);
                            hex.append(String.format("%08X", intValue));
                            break;
                        // Add more cases as needed for other data types
                    }
                }
            }
        }
        return hex.toString();
    }

    public String traducirCodigoAHex(){
        StringBuilder hex = new StringBuilder();
        String[] lines = codigoEnsamblador.split("\n");
        boolean codeSection = false;

        for (String line : lines) {
            line = line.trim();
            if (line.equalsIgnoreCase("MOV DS,AX")) {
                codeSection = true;
                continue;
            }
            if (codeSection) {
                if (line.isEmpty() || line.startsWith(".")) {
                    break;
                }
                String[] parts = line.split("\\s+");
                if (parts.length >= 2) {
                    String instruction = parts[0];
                    String operands = parts[1];
                    hex.append(instruction).append(" ");
                    switch (instruction.toLowerCase()) {
                        case "mov":
                            hex.append("B8");
                            break;
                        case "add":
                            hex.append("01");
                            break;
                        case "sub":
                            hex.append("29");
                            break;
                        case "mul":
                            hex.append("F7");
                            break;
                        case "div":
                            hex.append("F7");
                            break;
                        case "inc":
                            hex.append("40");
                            break;
                        case "dec":
                            hex.append("48");
                            break;
                        case "cmp":
                            hex.append("3B");
                            break;
                        case "jmp":
                            hex.append("E9");
                            break;
                        case "je":
                            hex.append("74");
                            break;
                        case "jne":
                            hex.append("75");
                            break;
                        case "jg":
                            hex.append("7F");
                            break;
                        case "jge":
                            hex.append("7D");
                            break;
                        case "jl":
                            hex.append("7C");
                            break;
                        case "jle":
                            hex.append("7E");
                            break;
                        case "call":
                            hex.append("E8");
                            break;
                        case "ret":
                            hex.append("C3");
                            break;
                        default:
                            break;

                    }
                    hex.append(" ");
                    if (instruction.equalsIgnoreCase("mov")) {
                        hex.append(String.format("%08X", Integer.parseInt(operands)));
                    } else {
                        hex.append(String.format("%02X", Integer.parseInt(operands)));
                    }
                    hex.append("\n");
                }
            }
        }
        return hex.toString();
    }

    public String getCodigoObjeto() {
        return codigoObjeto;
    }

}
