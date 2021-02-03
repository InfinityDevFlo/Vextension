/**
 *   Copyright © 2020 | vironlab.eu | All Rights Reserved.<p>
 * <p>
 *      ___    _______                        ______         ______  <p>
 *      __ |  / /___(_)______________ _______ ___  / ______ ____  /_ <p>
 *      __ | / / __  / __  ___/_  __ \__  __ \__  /  _  __ `/__  __ \<p>
 *      __ |/ /  _  /  _  /    / /_/ /_  / / /_  /___/ /_/ / _  /_/ /<p>
 *      _____/   /_/   /_/     \____/ /_/ /_/ /_____/\__,_/  /_.___/ <p>
 *<p>
 *    ____  _______     _______ _     ___  ____  __  __ _____ _   _ _____ <p>
 *   |  _ \| ____\ \   / / ____| |   / _ \|  _ \|  \/  | ____| \ | |_   _|<p>
 *   | | | |  _|  \ \ / /|  _| | |  | | | | |_) | |\/| |  _| |  \| | | |  <p>
 *   | |_| | |___  \ V / | |___| |__| |_| |  __/| |  | | |___| |\  | | |  <p>
 *   |____/|_____|  \_/  |_____|_____\___/|_|   |_|  |_|_____|_| \_| |_|  <p>
 *<p>
 *<p>
 *   This program is free software: you can redistribute it and/or modify<p>
 *   it under the terms of the GNU General Public License as published by<p>
 *   the Free Software Foundation, either version 3 of the License, or<p>
 *   (at your option) any later version.<p>
 *<p>
 *   This program is distributed in the hope that it will be useful,<p>
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of<p>
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<p>
 *   GNU General Public License for more details.<p>
 *<p>
 *   You should have received a copy of the GNU General Public License<p>
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.<p>
 *<p>
 *   Contact:<p>
 *<p>
 *     Discordserver:   https://discord.gg/wvcX92VyEH<p>
 *     Website:         https://vironlab.eu/ <p>
 *     Mail:            contact@vironlab.eu<p>
 *<p>
 */

package eu.vironlab.vextension.collection

import java.util.*


class TripleMap<K, F, S> {

    var wrapped: MutableMap<K, DataPair<F, S>> = mutableMapOf()

    fun entrySet(): Set<Map.Entry<K, DataPair<F, S>>> {
        return wrapped.entries
    }

    fun clear() {
        wrapped.clear()
    }

    fun size(): Int {
        return wrapped.size
    }

    fun add(key: K, valueF: F, valueS: S) {
        wrapped[key] = DataPair(valueF, valueS)
    }

    fun remove(key: K) {
        wrapped.remove(key)
    }

    fun keySet(): Set<K> {
        return wrapped.keys
    }

    operator fun contains(key: K): Boolean {
        return wrapped.containsKey(key)
    }

    operator fun get(key: K): Optional<DataPair<F, S>> {
        return Optional.ofNullable(wrapped[key])
    }

    fun getFirst(key: K): Optional<F> {
        if (wrapped.containsKey(key)) {
            return Optional.ofNullable(wrapped[key]!!.first)
        }else {
            return Optional.ofNullable(null)
        }
    }

    fun getSecond(key: K): Optional<S> {
        if (wrapped.containsKey(key)) {
            return Optional.ofNullable(wrapped[key]!!.second)
        }else {
            return Optional.ofNullable(null)
        }
    }

    fun replaceFirst(key: K, value: F) {
        if (wrapped.containsKey(key)) {
            wrapped[key]!!.first = value
        }
    }

    fun replaceSecond(key: K, value: S) {
        if (wrapped.containsKey(key)) {
            wrapped[key]!!.second = value
        }
    }

    fun replace(key: K, valueF: F, valueS: S) {
        if (wrapped.containsKey(key)) {
            wrapped[key]!!.first = valueF
            wrapped[key]!!.second = valueS
        }
    }


}