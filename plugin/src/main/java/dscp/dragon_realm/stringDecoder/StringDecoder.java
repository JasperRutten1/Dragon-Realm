package dscp.dragon_realm.stringDecoder;

import org.bukkit.ChatColor;

public class StringDecoder {

    /**
     * decodes a string using color codes in to a string that has colors
     *
     * @param string
     * the string that will be decoded using the colors,
     * example: "this GR{string} will be decoded to use LB{colors}"
     * @return
     * a decoded string using the colors
     */
    public static String colorDecode(String string){
        System.out.println("rec");
        if(!(string.contains(StringDecoderColor.openChar) && string.substring(string.indexOf(StringDecoderColor.openChar)).contains(StringDecoderColor.closeChar))){
            System.out.println("string does not contain { and/or }");
            return string;
        }
        if(string.length() < 4) return string;

        int openPos = string.indexOf(StringDecoderColor.openChar);
        System.out.println(openPos);
        int closePos = openPos + string.substring(openPos).indexOf(StringDecoderColor.closeChar);
        System.out.println(closePos);

        if(openPos < 2) return string.substring(0, openPos + 1) + colorDecode(string.substring(openPos + 1));
        String colorCode = string.substring(openPos - 2, openPos);
        System.out.println(colorCode);

        if(!isColorCode(colorCode)) return string.substring(0, openPos + 1) + colorDecode(string.substring(openPos + 1));
        else {
            System.out.println("layer");
            StringDecoderColor color = getColorFromCode(colorCode);
            assert color != null;

            StringBuilder sb = new StringBuilder();
            sb.append(string.substring(0, openPos - 2)).append(color.getChatColor()).append(string.substring(openPos + 1, closePos)).append(ChatColor.RESET);

            if(closePos == string.length() - 1) return sb.toString();
            else return sb.toString() + colorDecode(string.substring(closePos + 1));
        }
    }

    /**
     * checks if a given string is a valid color code
     * @param code
     * a string of 2 characters
     * @return
     * true if the given string (2 characters) is a valid color code,
     * false if not
     */
    private static boolean isColorCode(String code){
        for(StringDecoderColor color : StringDecoderColor.values()){
            if(code.equals(color.getCode())){
                return true;
            }
        }
        return false;
    }

    /**
     * get the color that matches with the color code
     * @param code
     * a string of 2 characters
     * @return
     * the StringDecoderColor that matches with the color code,
     * null if no color code matches
     */
    private static StringDecoderColor getColorFromCode(String code){
        for(StringDecoderColor color : StringDecoderColor.values()){
            if(code.equals(color.getCode())){
                return color;
            }
        }
        return null;
    }


}
