extern crate regex;

use std::io;
use std::io::prelude::*;
use regex::Regex;
use std::collections::HashMap;

fn main() {
    let mut lines = vec![];

    let stdin = io::stdin();
    for line in stdin.lock().lines() {
        let s = line.unwrap();
        lines.push(s);
    }

    lines.sort(); // chronologically

    let guards = read_guards(&lines);

    println!("Part 1: {}", solve_part1(&guards));
    println!("Part 2: {}", solve_part2(&guards));
}

fn solve_part1(guards: &HashMap<i32, [i32; 60]>) -> i32 {
    let mut chosen_id = 0;
    let mut max_sum = 0;
    let mut best_minute: usize = 0;

    for (k, v) in guards {
        let sum: i32 = v.iter().sum();

        if sum > max_sum {
            max_sum = sum;
            chosen_id = *k;
            best_minute = find_best_minute(v);
        }
    }

    chosen_id * (best_minute as i32)
}

fn solve_part2(guards: &HashMap<i32, [i32; 60]>) -> i32 {
    let mut chosen_id = 0;
    let mut max_slept = 0;
    let mut best_minute: usize = 0;

    for (k, v) in guards {
        let guard_best_minute = find_best_minute(v);

        if v[guard_best_minute] > max_slept {
            max_slept = v[guard_best_minute];
            chosen_id = *k;
            best_minute = guard_best_minute;
        }
    }

    chosen_id * (best_minute as i32)
}

fn read_guards(lines: &Vec<String>) -> HashMap<i32, [i32; 60]> {
    let re = Regex::new(r"\[\d\d\d\d-\d\d-\d\d \d\d:(\d\d)\] (Guard #(\d+) begins shift|falls asleep|wakes up)").unwrap();
    let mut guards = HashMap::new();
    let mut id = 0;
    let mut begin_minute: usize = 0;

    for line in lines.iter() {
        for cap in re.captures_iter(line) {
            if cap[2].starts_with("Guard") {
                id = parse_int(&cap[3]);
                guards.entry(id).or_insert([0; 60]);
            } else if &cap[2] == "falls asleep" {
                begin_minute = parse_usize(&cap[1]);
            } else if &cap[2] == "wakes up" {
                for i in begin_minute..parse_usize(&cap[1]) {
                    let guard = match guards.get_mut(&id) {
                        Some(guard) => guard,
                        None => panic!("no guard found"),
                    };
                    guard[i] += 1;
                }
            }
        }
    }

    guards
}

fn find_best_minute(minutes: &[i32]) -> usize {
    let mut max = 0;
    let mut max_index: usize = 0;

    for (i, minute) in minutes.iter().enumerate() {
        if *minute > max {
            max = *minute;
            max_index = i;
        }
    }

    max_index
}

fn parse_int(string: &str) -> i32 {
    match string.parse() {
        Ok(num) => num,
        Err(_) => 0,
    }
}

fn parse_usize(string: &str) -> usize {
    match string.parse() {
        Ok(num) => num,
        Err(_) => 0,
    }
}
