<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a @click="returnToStundenplan">Stundenplan</a>
				<span title="Kataloge">Kataloge</span>
			</nav>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :daten="schuljahresabschnittsauswahl" />
		</template>
		<template #header />
		<template #content>
			<div class="secondary-menu--navigation">
				<template v-for="child_item in children" :key="child_item.name">
					<svws-ui-menu-item @click="setChild(child_item)">
						<template #label> <span>{{ child_item.text }}</span> </template>
					</svws-ui-menu-item>
				</template>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { StundenplanKatalogeAuswahlProps } from "./SStundenplanKatalogeAuswahlProps";

	const props = defineProps<StundenplanKatalogeAuswahlProps>();

</script>
