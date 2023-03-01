<template>
	<svws-ui-secondary-menu>
		<template #headline>Kataloge</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #header />
		<template #content>
			<template v-for="item in routeKataloge.menu" :key="item.name">
				<div class="secondary-menu--navigation container">
					<svws-ui-sidebar-menu-item v-if="item.hatSchulform() && item.hatEineKompetenz()" @click="router.push({ name: item.name })">
						<template #label> <span>{{ item.text }}</span> </template>
					</svws-ui-sidebar-menu-item>
				</div>
			</template>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { router } from "~/router/RouteManager";
	import { routeKataloge } from "~/router/apps/RouteKataloge";
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
