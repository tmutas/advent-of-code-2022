import 'dart:io';

final Map<String, int> score_map = {
  'X' : 1,
  'Y' : 2,
  'Z' : 3
};

final Map<String, int> win_map = {
  // Map the ones you beat to the same number
  'X' : 0, // Rock beats Scissors
  'Y' : 1, // Scissors beat Paper
  'Z' : 2, // Paper beats rock
  'A' : 1, // Rock beats Scissors
  'B' : 2, // Scissors beat Paper
  'C' : 0 // Paper beats rock
};

final Map<String, int> draw_map = {
  // Map the ones you draw to the same number
  'X' : 0, 
  'Y' : 1, 
  'Z' : 2, 
  'A' : 0, 
  'B' : 1, 
  'C' : 2 
};


final Map<String, int> lose_map = {
  // Map the ones you lose to the same number
  'X' : 0, 
  'Y' : 1, 
  'Z' : 2, 
  'A' : 2, 
  'B' : 0, 
  'C' : 1 
};

final responses = ['X', 'Y', 'Z'];

String getResponse(final String enemy, final String goal){
  String response = '';
  switch(goal){
    case 'X':
      // Lose
      response = responses[lose_map[enemy] ?? 0];
      break;
    case 'Y':
      response = responses[draw_map[enemy] ?? 0];
      break;
    case 'Z':
      response = responses[win_map[enemy] ?? 0];
      break;
  } 
  return response;
}

int outcome(final String enemy, final String you){
  if (draw_map[enemy] == draw_map[you]){
    return 3;
  } else if (win_map[enemy] == win_map[you]){
    return 6;
  } else return 0;
}

int play(final String enemy, final String you){
  final int outcome_value = outcome(enemy, you);
  final int score = score_map[you] ?? 0;
  return score + outcome_value;
}

int play2(final String enemy, final String goal){
  String you = getResponse(enemy, goal);
  final int outcome_value = outcome(enemy, you);
  final int score = score_map[you] ?? 0;
  return score + outcome_value;
}

void main() async {

  var config = File('input.txt');

  var lines = await config.readAsLines();
  print('The file is ${lines.length} lines long.');

  // Part 1
  var score = 0;

  // Part 2
  var score2 = 0;
  for (final line in lines){
    var split_line = line.split(' ');
    var enemy = split_line[0];
    var you = split_line[1];

    var result = play(enemy, you);
    score += result;

    var result2 = play2(enemy, you);
    score2 += result2;
  }
  print('Final score: $score');
  print('Final score Part 2: $score2');

}