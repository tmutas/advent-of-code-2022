import 'dart:io';

int getCharacterValue(final String char){
  // a : 97 gets value 1
  // z : 122 gets value 26 
  // A : 65 get
  // Z : 90
  final value = char.codeUnits[0];
  if (value >= 97){
    return value - 97 + 1;
  } else {
    return value - 65 + 27;
  }
}

List<String> splitCompartments(String input){
  return [
    input.substring(0, input.length ~/ 2),
    input.substring(input.length ~/ 2, input.length),
  ];
}

void main() async {
  var config = File('input.txt');

  var lines = await config.readAsLines();

  // Part 1
  int score = 0;
  
  for (final line in lines){
    final compartments = splitCompartments(line);
    
    for (int i = 0; i < compartments[0].length; i++){
      var char = compartments[0][i];
      if (compartments[1].contains(char)){
        score += getCharacterValue(char);
        break;
      }
    }
  }
  print('Part 1: $score');

  // Part 2
  int score2 = 0;

  for (int i = 0; i < lines.length; i += 3){
    for (int j = 0; j < lines[i].length; j++){
      var char = lines[i][j];
      if (lines[i + 1].contains(char) && lines[i + 2].contains(char)){
        score2 += getCharacterValue(char);
        break;
      }
    }
  }
  print('Part 2: $score2');

}