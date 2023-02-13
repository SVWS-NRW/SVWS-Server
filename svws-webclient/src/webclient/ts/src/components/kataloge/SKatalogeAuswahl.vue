<template>
	<svws-ui-secondary-menu>
		<template #headline>Kataloge</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" />
		</template>
		<template #header />
		<template #content>
			<div class="secondary-menu--navigation container">
				<svws-ui-sidebar-menu-item v-for="item in routeKataloge.menu" :key="item.name" @click="router.push({ name: item.name })">
					<template #label> <span>{{ item.text }}</span> </template>
				</svws-ui-sidebar-menu-item>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { router } from "~/router";
	import { routeKataloge } from "~/router/apps/RouteKataloge";
	import { List, Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";

	const props = defineProps<{
		abschnitte: List<Schuljahresabschnitt>;
		aktAbschnitt: Schuljahresabschnitt;
		setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	}>();

	const menu_items = [
		{ title: "Fächer", value: "faecher" },
		{ title: "Jahrgänge", value: "jahrgaenge" },
		{ title: "Religionen", value: "religionen" },
		{ title: "Förderschwerpunkte", value: "foerderschwerpunkte" },
		{ title: "Haltestellen", value: "haltestellen" },
		{ title: "Betriebe", value: "betriebe" }
	];

</script>
