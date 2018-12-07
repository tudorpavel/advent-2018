extern crate regex;

use std::io;
use std::io::prelude::*;
use regex::Regex;
use std::collections::BTreeMap;

fn main() {
    let mut lines = vec![];

    let stdin = io::stdin();
    for line in stdin.lock().lines() {
        let s = line.unwrap();
        lines.push(s);
    }

    let graph = make_graph(lines);

    println!("Part 1: {}", solve_part1(&graph));
    println!("Part 2: {}", solve_part2(&graph));
}

fn solve_part1(graph: &BTreeMap<String, Vec<String>>) -> String {
    let mut graph = graph.clone();
    let mut result = String::from("");

    while graph.len() > 0 {
        let mut candidate_id = String::from("");

        for (id, parents) in &graph {
            let mut is_candidate = true;

            for parent in parents {
                if graph.contains_key(parent) {
                    is_candidate = false;
                    break;
                }
            }

            if is_candidate {
                result.push_str(id.as_str());
                candidate_id = id.clone();
                break;
            }
        }

        graph.remove(&candidate_id);
    }

    result
}

fn solve_part2(graph: &BTreeMap<String, Vec<String>>) -> i32 {
    const WORKER_COUNT: usize = 5;
    const EXTRA_SECONDS: i32 = 60;

    let mut graph = graph.clone();
    let mut tick_count = 0;
    let mut workers = [0; WORKER_COUNT];
    let mut worker_job = vec![String::from(""); WORKER_COUNT];

    loop {
        for i in 0..workers.len() {
            if workers[i] == 0 {
                if worker_job[i] != "" {
                    graph.remove(&worker_job[i]);
                    worker_job[i] = String::from("");
                }

                for (id, parents) in graph.clone() {
                    let mut is_candidate = true;

                    for parent in parents {
                        if graph.contains_key(&parent) {
                            is_candidate = false;
                            break;
                        }
                    }

                    if is_candidate {
                        for running_job in worker_job.iter() {
                            if *id == *running_job {
                                is_candidate = false;
                                break;
                            }
                        }
                    }

                    if is_candidate {
                        worker_job[i] = id.clone();
                        workers[i] = EXTRA_SECONDS + alphabet_index(&id);
                        break;
                    }
                }
            } else {
                workers[i] -= 1;
            }
        }

        if graph.len() <= 0 {
            break;
        }

        tick_count += 1;
    }

    tick_count
}

fn make_graph(lines: Vec<String>) -> BTreeMap<String, Vec<String>> {
    let mut graph = BTreeMap::new();
    let re = Regex::new(r"Step (\w) must be finished before step (\w) can begin.").unwrap();

    for line in lines {
        for cap in re.captures_iter(line.as_str()) {
            let parent_id = String::from(&cap[1]);
            let child_id = String::from(&cap[2]);

            graph.entry(parent_id.clone()).or_insert(vec![]);
            graph.entry(child_id.clone()).or_insert(vec![]);

            let child = graph.get_mut(&child_id).unwrap();
            &child.push(parent_id.clone());
        }
    }

    graph
}

fn alphabet_index(s: &String) -> i32 {
    let c = s.chars().next().unwrap();

    (c as i32) - (b'A' as i32)
}
