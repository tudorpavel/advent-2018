# frozen_string_literal: true

ids = ARGF.readlines
id_map = {}

ids.each do |id|
  id_map[id] = id.chars.group_by(&:itself).transform_values(&:size)
end

checksum1 = id_map.count { |h| h[1].select { |_, v| v == 2 } != {} }
checksum2 = id_map.count { |h| h[1].select { |_, v| v == 3 } != {} }

puts "Normal: #{checksum1 * checksum2}"
