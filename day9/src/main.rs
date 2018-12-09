extern crate regex;

use std::io;
use std::io::prelude::*;
use regex::Regex;

fn main() {
    let mut lines = vec![];

    let stdin = io::stdin();
    for line in stdin.lock().lines() {
        let s = line.unwrap();
        lines.push(s);
    }

    let (player_count, last_marble) = read_input(&lines[0]);

    println!("Part 1: {}", solve_part1(player_count, last_marble));
    // println!("Part 2: {}", solve_part1(player_count, 100 * last_marble));
}

fn solve_part1(player_count: i32, last_marble: i32) -> i32 {
    let mut player_scores = vec![0; player_count as usize];
    let mut circle = vec![0; 1];
    let mut current_index = 0;

    for i in 1..=last_marble {
        // println!("marble: {}", i);
        // println!("current: {}", current_index);
        // println!("player_scores: {:?}", player_scores);
        // println!("circle: {:?}", circle);

        if i % 23 == 0 {
            let current_player = (i - 1) as usize % player_count as usize;
            player_scores[current_player] += i;

            current_index = circle_offset(current_index, -7, circle.len());
            let removed_marble = circle.remove(current_index);
            // println!("(marble, len, value): ({}, {}, {})", i, circle.len()+1, removed_marble);
            player_scores[current_player] += removed_marble;
        } else {
            current_index = circle_offset(current_index, 2, circle.len());
            circle.insert(current_index, i);
        }
    }

    *player_scores.iter().max().unwrap()
}

fn read_input(line: &str) -> (i32, i32) {
    let re = Regex::new(r"(\d+) players; last marble is worth (\d+) points").unwrap();

    let mut player_count = 0;
    let mut last_marble = 0;

    for cap in re.captures_iter(line) {
        player_count = cap[1].parse().unwrap();
        last_marble = cap[2].parse().unwrap();
    }

    (player_count, last_marble)
}

fn circle_offset(index: usize, offset: i32, len: usize) -> usize {
    let result = index as i32 + offset;

    if result >= 0 {
        result as usize % len
    } else {
        (len as i32 + result % len as i32) as usize
    }
}
