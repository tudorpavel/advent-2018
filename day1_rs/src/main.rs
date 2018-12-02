use std::io;
use std::io::prelude::*;
use std::collections::HashMap;

fn main() {
    let mut nums = vec![];

    let stdin = io::stdin();
    for line in stdin.lock().lines() {
        let n: i32 = match line.unwrap().parse() {
            Ok(num) => num,
            Err(_) => continue,
        };

        nums.push(n);
    }

    println!("Part 1: {}", sum(&nums));
    println!("Part 2: {}", find_repeat_frequency(&nums));
}

fn sum(nums: &Vec<i32>) -> i32 {
    nums.iter().sum()
}

fn find_repeat_frequency(nums: &Vec<i32>) -> i32 {
    let mut partial_sums = HashMap::new();
    let mut sum = 0;

    loop {
        for num in nums.iter() {
            sum += num;
            if partial_sums.contains_key(&sum) {
                return sum;
            }
            partial_sums.insert(sum, true);
        }
    }
}
