<template>
	<Story title="Tooltip" id="svws-ui-tooltip" :layout="{type: 'grid', width: '45%'}" icon="ri:chat-4-line">
		<Variant title="Default" id="Default">
			<svws-ui-tooltip>
				Default Tooltip
				<template #content>
					Tooltip Content
				</template>
			</svws-ui-tooltip>
		</Variant>
		<Variant title="Fixed Position" id="Fixed Position">
			<svws-ui-tooltip position="right">
				Tooltip auf der rechten Seite
				<template #content>
					Tooltip Content
				</template>
			</svws-ui-tooltip>
		</Variant>
		<Variant title="Without Arrow" id="Without Arrow">
			<svws-ui-tooltip :show-arrow="false">
				Tooltip Bubble ohne Arrow
				<template #content>
					Tooltip Content
				</template>
			</svws-ui-tooltip>
		</Variant>
		<Variant title="With Indicator Icons" id="With Indicator Icons">
			<div class="flex flex-col gap-2 items-start">
				<svws-ui-tooltip indicator="help">
					Mit Help Icon
					<template #content>
						Tooltip Content
					</template>
				</svws-ui-tooltip>
				<svws-ui-tooltip indicator="info">
					Mit Info Icon
					<template #content>
						Tooltip Content
					</template>
				</svws-ui-tooltip>
				<svws-ui-tooltip indicator="danger" color="danger">
					Mit Danger Icon
					<template #content>
						Tooltip Content
					</template>
				</svws-ui-tooltip>
			</div>
		</Variant>
		<Variant title="Disabled Hover" id="Disabled Hover">
			<svws-ui-tooltip :hover="false">
				Zum Öffnen klicken
				<template #content>
					Tooltip Content
				</template>
			</svws-ui-tooltip>
		</Variant>
		<Variant title="Color Dark" id="Color Dark">
			<svws-ui-tooltip color="dark">
				Dark Tooltip
				<template #content>
					Tooltip Content
				</template>
			</svws-ui-tooltip>
		</Variant>
		<Variant title="Color Primary" id="Color Primary">
			<svws-ui-tooltip color="primary">
				Primary Color Tooltip
				<template #content>
					Tooltip Content
				</template>
			</svws-ui-tooltip>
		</Variant>
		<Variant title="Color Danger" id="Color Danger">
			<svws-ui-tooltip color="danger">
				Danger Color Tooltip
				<template #content>
					Tooltip Content
				</template>
			</svws-ui-tooltip>
		</Variant>
	</Story>
</template>
<script setup lang="ts">
</script>
