import 'dart:io';

void main() async {
  var config = File('input.txt');

  var lines = await config.readAsLines();
  print('The file is ${lines.length} lines long.');


  List<int> elves = [];

  var current_elf = 0;

  for (final line in lines){
      if (line == '') {
        elves.add(current_elf);
        current_elf=0;
      } else {
        current_elf += int.parse(line);
      }
  }
  
  elves.add(current_elf);
  elves.sort();

  final int top = elves[0];
  final int bottom = elves[elves.length - 1];
  
  print('First sorted element: $top');
  print('Last sorted element: $bottom');

  // Part Two
  var top3 = 0;
  for (var i = 0; i < 3; i++){
    top3 += elves[elves.length - 1 - i];
  }

  print('Top 3 Elves: $top3');
}