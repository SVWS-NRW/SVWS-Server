<template>
	<svws-ui-content-card title="Zeitraster">
		<StundenplanWoche :map-tage="mapTage" />
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { List, StundenplanManager, StundenplanPausenzeit, StundenplanZeitraster} from "@core";
	import { Wochentag } from "@core";
	import { computed, ref } from "vue";

	const props = defineProps<{
		stundenplanManager: () => StundenplanManager;
	}>();

	const mapTage = computed(()=>{
		const stunden = props.stundenplanManager().getListZeitraster()
		const map = new Map<number, StundenplanZeitraster[]>();
		for (const s of stunden) {
			if (!map.has(s.wochentag))
				map.set(s.wochentag, []);
			const a = map.get(s.wochentag)!;
			a.push(s);
		}
		return map;
	})

</script>
