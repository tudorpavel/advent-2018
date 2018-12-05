use std::io;
use std::io::prelude::*;

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

// TODO: this is super inefficient, find a better algorithm
fn solve_part1(polymer: &str) -> usize {
    let mut last_polymer = String::from(polymer);

    loop {
        let new_polymer = remove_first_pair(&last_polymer);

        if new_polymer == last_polymer {
            return new_polymer.len();
        }

        last_polymer = new_polymer;
    }
}

fn remove_first_pair(polymer: &String) -> String {
    let mut prev_char = polymer.chars().next().unwrap();

    for (i, character) in polymer[1..].chars().enumerate() {
        // a a => false
        // a A => true
        // A a => true
        // A A => false
        let different = prev_char.to_string() != character.to_string();
        let same_upper = prev_char.to_string() == character.to_uppercase().to_string();
        let same_lower = prev_char.to_string() == character.to_lowercase().to_string();

        if different && (same_upper || same_lower) {
            let (mut first, last) = polymer.split_at(i);

            return String::from(first) + &last[2..];
        }

        prev_char = character;
    }

    polymer.clone()
}
