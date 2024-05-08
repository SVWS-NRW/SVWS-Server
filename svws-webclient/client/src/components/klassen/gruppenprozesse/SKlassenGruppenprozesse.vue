<template>
	<div class="page--content">
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<svws-ui-content-card title="Alle Klassen">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-button>TODO</svws-ui-button>
					<svws-ui-spacing />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-content-card :title="'Klassen ' + strKlassen">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-button>TODO</svws-ui-button>
					<svws-ui-spacing />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { KlassenGruppenprozesseProps } from "./SKlassenGruppenprozesseProps";

	const props = defineProps<KlassenGruppenprozesseProps>();

	const strKlassen = computed<string>(() => {
		const liste = props.klassenListeManager().liste.auswahl();
		let s = "";
		for (const kl of liste)
			s += (s.length > 0 ? ", " : "") + kl.kuerzel;
		return s;
	});

</script>
