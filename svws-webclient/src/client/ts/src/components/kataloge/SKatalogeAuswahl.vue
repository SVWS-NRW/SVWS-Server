<template>
	<svws-ui-secondary-menu>
		<template #headline>Kataloge</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #header />
		<template #content>
			<template v-for="child_item in children" :key="child_item.name">
				<div class="secondary-menu--navigation container">
					<svws-ui-menu-item @click="setChild(child_item)">
						<template #label> <span>{{ child_item.text }}</span> </template>
					</svws-ui-menu-item>
				</div>
			</template>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { KatalogeAuswahlProps } from "./SKatalogeAuswahlProps";

	const props = defineProps<KatalogeAuswahlProps>();

	const menu_items = [
		{ title: "Fächer", value: "faecher" },
		{ title: "Jahrgänge", value: "jahrgaenge" },
		{ title: "Religionen", value: "religionen" },
		{ title: "Förderschwerpunkte", value: "foerderschwerpunkte" },
		{ title: "Haltestellen", value: "haltestellen" },
		{ title: "Betriebe", value: "betriebe" }
	];

</script>
