# frozen_string_literal: true

def make_node(marble)
  {
    marble: marble,
    left: nil,
    right: nil
  }
end

def move_right(node, index)
  return node if index.zero?

  move_right(node[:right], index - 1)
end

def move_left(node, index)
  return node if index.zero?

  move_left(node[:left], index - 1)
end

def insert_before(node, new_node)
  node[:left][:right] = new_node
  new_node[:left] = node[:left]
  new_node[:right] = node
  node[:left] = new_node
end

def delete(node)
  node[:left][:right] = node[:right]
  node[:right][:left] = node[:left]
  node[:right]
end

def right_print(node, index)
  return if index.zero?

  print "#{node.marble}, "
  right_print(node[:right], index - 1)
end

def calculate_high_score(player_count, marble_count)
  player_scores = Array.new(player_count, 0)

  cursor = make_node(0)
  cursor[:left] = cursor
  cursor[:right] = cursor

  1.upto(marble_count) do |marble|
    # right_print(cursor, 20)
    # puts

    if (marble % 23).zero?
      cursor = move_left(cursor, 7)
      # puts "deleted value: #{cursor.marble}"
      player_scores[marble % player_count] += marble + cursor[:marble]
      cursor = delete(cursor)
    else
      cursor = move_right(cursor, 2)
      # puts cursor.marble
      cursor = insert_before(cursor, make_node(marble))
    end
  end

  player_scores.max
end

matches = /(\d+) players; last marble is worth (\d+) points/.match(gets)

player_count = matches[1].to_i
marble_count = matches[2].to_i

puts "Part 1: #{calculate_high_score(player_count, marble_count)}"
puts "Part 2: #{calculate_high_score(player_count, 100 * marble_count)}"
