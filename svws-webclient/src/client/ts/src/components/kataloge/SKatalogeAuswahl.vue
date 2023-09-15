<template>
	<svws-ui-secondary-menu>
		<template #headline>Kataloge</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #header />
		<template #content>
			<div class="secondary-menu--navigation">
				<template v-for="child_item in children" :key="child_item.name">
					<svws-ui-menu-item @click="setChild(child_item)">
						<template #label> <span>{{ child_item.text }}</span> </template>
					</svws-ui-menu-item>
					<svws-ui-spacing v-if="child_item.name === 'kataloge.foerderschwerpunkte' || child_item.name === 'kataloge.zeitraster'" />
				</template>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { KatalogeAuswahlProps } from "./SKatalogeAuswahlProps";

	const props = defineProps<KatalogeAuswahlProps>();

</script>
