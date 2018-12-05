use std::io;
use std::io::prelude::*;

const DISTANCE_SAME_LETTER: i32 = (b'a' - b'A') as i32;

fn main() {
    let mut lines = vec![];

    let stdin = io::stdin();
    for line in stdin.lock().lines() {
        let s = line.unwrap();
        lines.push(s);
    }

    let polymer = &lines[0];

    println!("Part 1: {}", solve_part1(&polymer));
    // println!("Part 2: {}", solve_part2(&polymer));
}

fn solve_part1(polymer: &str) -> usize {
    let mut stack: Vec<char> = Vec::new();

    for character in polymer.chars() {
        let top = match stack.last().cloned() {
            Some(c) => c,
            None => '0',
        };

        if is_unit(&top, &character)  {
            stack.pop();
        } else {
            stack.push(character);
        }
    }

    stack.len()
}

fn is_unit(c1: &char, c2: &char) -> bool {
    ((*c1 as i32) - (*c2 as i32)).abs() == DISTANCE_SAME_LETTER
}
